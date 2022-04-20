package pt.tecnico.supplier.client;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.InputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.supplier.grpc.ProductsRequest;
import pt.tecnico.supplier.grpc.SignedResponse;
import pt.tecnico.supplier.grpc.SupplierGrpc;

public class SupplierClient {

	/**
	 * Set flag to true to print debug messages. The flag can be set using the
	 * -Ddebug command line option.
	 */
	private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
	private static final String DIGEST_ALGO = "SHA-256";
	private static final String SYM_CIPHER = "AES/ECB/PKCS5Padding";
	private static SecretKey KEY;
	/** Helper method to print debug messages. */
	private static void debug(String debugMessage) {
		if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}
	public static SecretKeySpec readKey(String resourcePathName) throws Exception {
		System.out.println("Reading key from resource " + resourcePathName + " ...");
		System.out.println(System.getProperty("user.dir")+"");
		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePathName);
		byte[] encoded = new byte[fis.available()];
		fis.read(encoded);
		fis.close();
		
		System.out.println("Key:");
		System.out.println(printHexBinary(encoded));
		SecretKeySpec keySpec = new SecretKeySpec(encoded, "AES");

		return keySpec;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(SupplierClient.class.getSimpleName() + " starting ...");
		KEY = readKey("secret.key");
		// Receive and print arguments.
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// Check arguments.
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s host port%n", SupplierClient.class.getName());
			return;
		}

		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final String target = host + ":" + port;
		debug(readKey("secret.key").toString());
		// Channel is the abstraction to connect to a service end-point.
		final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

		// Create a blocking stub for making synchronous remote calls.
		SupplierGrpc.SupplierBlockingStub stub = SupplierGrpc.newBlockingStub(channel);

		// Prepare request.
		ProductsRequest request = ProductsRequest.newBuilder().build();
		System.out.println("Request to send:");
		System.out.println(request.toString());
		debug("in binary hexadecimals:");
		byte[] requestBinary = request.toByteArray();
		debug(printHexBinary(requestBinary));
		debug(String.format("%d bytes%n", requestBinary.length));

		// Make the call using the stub.
		System.out.println("Remote call...");
		SignedResponse response = stub.listProducts(request);
		byte[] value= response.getSignature().getValue().toByteArray();
		byte[] responseByte = response.getResponse().toByteArray();
		redigestDecipherAndCompare(value, responseByte);
		// Print response.
		System.out.println("Received response:");
		System.out.println(response.toString());
		debug("in binary hexadecimals:");
		byte[] responseBinary = response.toByteArray();
		debug(printHexBinary(responseBinary));
		debug(String.format("%d bytes%n", responseBinary.length));
		System.out.println("Accpeted with Nounce: "+response.getSignature().getNounce());

		// A Channel should be shutdown before stopping the process.
		channel.shutdownNow();
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the to
	 * deciphered digest
	 */
	private static boolean redigestDecipherAndCompare(byte[] cipherDigest, byte[] bytes)
			throws Exception {

		// get a message digest object using the specified algorithm
		MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGO);

		// calculate the digest and print it out
		messageDigest.update(bytes);
		byte[] digest = messageDigest.digest();
		System.out.println("New digest:");
		System.out.println(printHexBinary(digest));

		// get an AES cipher object
		Cipher cipher = Cipher.getInstance(SYM_CIPHER);

		// decipher digest using the public key
		cipher.init(Cipher.DECRYPT_MODE, KEY);
		byte[] decipheredDigest = cipher.doFinal(cipherDigest);
		System.out.println("Deciphered Digest:");
		System.out.println(printHexBinary(decipheredDigest));

		// compare digests
		if (digest.length != decipheredDigest.length)
			return false;

		for (int i = 0; i < digest.length; i++)
			if (digest[i] != decipheredDigest[i])
				return false;
		return true;
	}

}
