// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: AddressBook.proto

package pt.tecnico.addressbook.grpc;

public interface PersonInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:pt.tecnico.addressbook.grpc.PersonInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>string email = 3;</code>
   * @return The email.
   */
  java.lang.String getEmail();
  /**
   * <code>string email = 3;</code>
   * @return The bytes for email.
   */
  com.google.protobuf.ByteString
      getEmailBytes();

  /**
   * <code>.pt.tecnico.addressbook.grpc.PersonInfo.PhoneNumber phone = 4;</code>
   * @return Whether the phone field is set.
   */
  boolean hasPhone();
  /**
   * <code>.pt.tecnico.addressbook.grpc.PersonInfo.PhoneNumber phone = 4;</code>
   * @return The phone.
   */
  pt.tecnico.addressbook.grpc.PersonInfo.PhoneNumber getPhone();
  /**
   * <code>.pt.tecnico.addressbook.grpc.PersonInfo.PhoneNumber phone = 4;</code>
   */
  pt.tecnico.addressbook.grpc.PersonInfo.PhoneNumberOrBuilder getPhoneOrBuilder();
}
