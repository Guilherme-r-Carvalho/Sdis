package pt.tecnico.grpc.server;

import pt.tecnico.grpc.Banking.*;
import pt.tecnico.grpc.BankingServiceGrpc;

import io.grpc.stub.StreamObserver;

import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.ABORTED;

public class BankingServiceImpl extends BankingServiceGrpc.BankingServiceImplBase {
	private Bank bank = new Bank();

	@Override
	public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
		bank.register(request.getClient(), request.getBalance());

		responseObserver.onNext(RegisterResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}
	@Override
	public void consult(ConsultRequest request, StreamObserver<ConsultResponse> responseObserver) {
		if (bank.isClient(request.getClient()) == false) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription("Input has to be a valid user!").asRuntimeException());
		}
		Integer balance = bank.getBalance(request.getClient());
		ConsultResponse response = ConsultResponse.newBuilder().setBalance(balance).build();

	    responseObserver.onNext(response);
	    responseObserver.onCompleted();
	}
	public void subsidize(SubsidizeRequest request,StreamObserver<SubsidizeResponse> rObserver) {
		if (!bank.getSubsidize(request.getThreshold(), request.getAmount())) {
			rObserver.onError(ABORTED.withDescription("One or more accounts can not get the subsidize!").asRuntimeException());
		}

		rObserver.onNext(SubsidizeResponse.getDefaultInstance());
		rObserver.onCompleted();
	}
}
