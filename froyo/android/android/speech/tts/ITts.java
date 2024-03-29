/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Workspaces\\MyEclipse9\\froyo\\src\\android\\speech\\tts\\ITts.aidl
 */
package android.speech.tts;
/**
 * AIDL for the TTS Service
 * ITts.java is autogenerated from this.
 *
 * {@hide}
 */
public interface ITts extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.speech.tts.ITts
{
private static final java.lang.String DESCRIPTOR = "android.speech.tts.ITts";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.speech.tts.ITts interface,
 * generating a proxy if needed.
 */
public static android.speech.tts.ITts asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.speech.tts.ITts))) {
return ((android.speech.tts.ITts)iin);
}
return new android.speech.tts.ITts.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setSpeechRate:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _result = this.setSpeechRate(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setPitch:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _result = this.setPitch(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_speak:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
int _result = this.speak(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isSpeaking:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isSpeaking();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.stop(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addSpeech:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
this.addSpeech(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_addSpeechFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.addSpeechFile(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_getLanguage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getLanguage();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_isLanguageAvailable:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
int _result = this.isLanguageAvailable(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setLanguage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
int _result = this.setLanguage(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_synthesizeToFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String[] _arg2;
_arg2 = data.createStringArray();
java.lang.String _arg3;
_arg3 = data.readString();
boolean _result = this.synthesizeToFile(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_playEarcon:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
int _result = this.playEarcon(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addEarcon:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
this.addEarcon(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_addEarconFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.addEarconFile(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.speech.tts.ITtsCallback _arg1;
_arg1 = android.speech.tts.ITtsCallback.Stub.asInterface(data.readStrongBinder());
int _result = this.registerCallback(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.speech.tts.ITtsCallback _arg1;
_arg1 = android.speech.tts.ITtsCallback.Stub.asInterface(data.readStrongBinder());
int _result = this.unregisterCallback(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_playSilence:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
int _result = this.playSilence(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setEngineByPackageName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.setEngineByPackageName(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDefaultEngine:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getDefaultEngine();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_areDefaultsEnforced:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.areDefaultsEnforced();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.speech.tts.ITts
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public int setSpeechRate(java.lang.String callingApp, int speechRate) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeInt(speechRate);
mRemote.transact(Stub.TRANSACTION_setSpeechRate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int setPitch(java.lang.String callingApp, int pitch) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeInt(pitch);
mRemote.transact(Stub.TRANSACTION_setPitch, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int speak(java.lang.String callingApp, java.lang.String text, int queueMode, java.lang.String[] params) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(text);
_data.writeInt(queueMode);
_data.writeStringArray(params);
mRemote.transact(Stub.TRANSACTION_speak, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean isSpeaking() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSpeaking, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int stop(java.lang.String callingApp) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void addSpeech(java.lang.String callingApp, java.lang.String text, java.lang.String packageName, int resId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(text);
_data.writeString(packageName);
_data.writeInt(resId);
mRemote.transact(Stub.TRANSACTION_addSpeech, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void addSpeechFile(java.lang.String callingApp, java.lang.String text, java.lang.String filename) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(text);
_data.writeString(filename);
mRemote.transact(Stub.TRANSACTION_addSpeechFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public java.lang.String[] getLanguage() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLanguage, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int isLanguageAvailable(java.lang.String language, java.lang.String country, java.lang.String variant, java.lang.String[] params) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(language);
_data.writeString(country);
_data.writeString(variant);
_data.writeStringArray(params);
mRemote.transact(Stub.TRANSACTION_isLanguageAvailable, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int setLanguage(java.lang.String callingApp, java.lang.String language, java.lang.String country, java.lang.String variant) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(language);
_data.writeString(country);
_data.writeString(variant);
mRemote.transact(Stub.TRANSACTION_setLanguage, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean synthesizeToFile(java.lang.String callingApp, java.lang.String text, java.lang.String[] params, java.lang.String outputDirectory) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(text);
_data.writeStringArray(params);
_data.writeString(outputDirectory);
mRemote.transact(Stub.TRANSACTION_synthesizeToFile, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int playEarcon(java.lang.String callingApp, java.lang.String earcon, int queueMode, java.lang.String[] params) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(earcon);
_data.writeInt(queueMode);
_data.writeStringArray(params);
mRemote.transact(Stub.TRANSACTION_playEarcon, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void addEarcon(java.lang.String callingApp, java.lang.String earcon, java.lang.String packageName, int resId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(earcon);
_data.writeString(packageName);
_data.writeInt(resId);
mRemote.transact(Stub.TRANSACTION_addEarcon, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void addEarconFile(java.lang.String callingApp, java.lang.String earcon, java.lang.String filename) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeString(earcon);
_data.writeString(filename);
mRemote.transact(Stub.TRANSACTION_addEarconFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int registerCallback(java.lang.String callingApp, android.speech.tts.ITtsCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int unregisterCallback(java.lang.String callingApp, android.speech.tts.ITtsCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int playSilence(java.lang.String callingApp, long duration, int queueMode, java.lang.String[] params) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callingApp);
_data.writeLong(duration);
_data.writeInt(queueMode);
_data.writeStringArray(params);
mRemote.transact(Stub.TRANSACTION_playSilence, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int setEngineByPackageName(java.lang.String enginePackageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(enginePackageName);
mRemote.transact(Stub.TRANSACTION_setEngineByPackageName, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getDefaultEngine() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultEngine, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean areDefaultsEnforced() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_areDefaultsEnforced, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setSpeechRate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setPitch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_speak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_isSpeaking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_addSpeech = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_addSpeechFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getLanguage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_isLanguageAvailable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setLanguage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_synthesizeToFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_playEarcon = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_addEarcon = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_addEarconFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_playSilence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_setEngineByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_getDefaultEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_areDefaultsEnforced = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
}
public int setSpeechRate(java.lang.String callingApp, int speechRate) throws android.os.RemoteException;
public int setPitch(java.lang.String callingApp, int pitch) throws android.os.RemoteException;
public int speak(java.lang.String callingApp, java.lang.String text, int queueMode, java.lang.String[] params) throws android.os.RemoteException;
public boolean isSpeaking() throws android.os.RemoteException;
public int stop(java.lang.String callingApp) throws android.os.RemoteException;
public void addSpeech(java.lang.String callingApp, java.lang.String text, java.lang.String packageName, int resId) throws android.os.RemoteException;
public void addSpeechFile(java.lang.String callingApp, java.lang.String text, java.lang.String filename) throws android.os.RemoteException;
public java.lang.String[] getLanguage() throws android.os.RemoteException;
public int isLanguageAvailable(java.lang.String language, java.lang.String country, java.lang.String variant, java.lang.String[] params) throws android.os.RemoteException;
public int setLanguage(java.lang.String callingApp, java.lang.String language, java.lang.String country, java.lang.String variant) throws android.os.RemoteException;
public boolean synthesizeToFile(java.lang.String callingApp, java.lang.String text, java.lang.String[] params, java.lang.String outputDirectory) throws android.os.RemoteException;
public int playEarcon(java.lang.String callingApp, java.lang.String earcon, int queueMode, java.lang.String[] params) throws android.os.RemoteException;
public void addEarcon(java.lang.String callingApp, java.lang.String earcon, java.lang.String packageName, int resId) throws android.os.RemoteException;
public void addEarconFile(java.lang.String callingApp, java.lang.String earcon, java.lang.String filename) throws android.os.RemoteException;
public int registerCallback(java.lang.String callingApp, android.speech.tts.ITtsCallback cb) throws android.os.RemoteException;
public int unregisterCallback(java.lang.String callingApp, android.speech.tts.ITtsCallback cb) throws android.os.RemoteException;
public int playSilence(java.lang.String callingApp, long duration, int queueMode, java.lang.String[] params) throws android.os.RemoteException;
public int setEngineByPackageName(java.lang.String enginePackageName) throws android.os.RemoteException;
public java.lang.String getDefaultEngine() throws android.os.RemoteException;
public boolean areDefaultsEnforced() throws android.os.RemoteException;
}
