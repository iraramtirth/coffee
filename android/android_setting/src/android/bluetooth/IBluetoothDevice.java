/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.bluetooth;

import java.util.UUID;

import android.annotation.SdkConstant;
import android.annotation.SdkConstant.SdkConstantType;

/**
 * Represents a remote Bluetooth device. A {@link BluetoothDevice} lets you
 * create a connection with the repective device or query information about
 * it, such as the name, address, class, and bonding state.
 *
 * <p>This class is really just a thin wrapper for a Bluetooth hardware
 * address. Objects of this class are immutable. Operations on this class
 * are performed on the remote Bluetooth hardware address, using the
 * {@link BluetoothAdapter} that was used to create this {@link
 * BluetoothDevice}.
 *
 * <p>To get a {@link BluetoothDevice}, use
 * {@link BluetoothAdapter#getRemoteDevice(String)
 * BluetoothAdapter.getRemoteDevice(String)} to create one representing a device
 * of a known MAC address (which you can get through device discovery with
 * {@link BluetoothAdapter}) or get one from the set of bonded devices
 * returned by {@link BluetoothAdapter#getBondedDevices()
 * BluetoothAdapter.getBondedDevices()}. You can then open a
 * {@link BluetoothSocket} for communciation with the remote device, using
 * {@link #createRfcommSocketToServiceRecord(UUID)}.
 *
 * <p class="note"><strong>Note:</strong>
 * Requires the {@link android.Manifest.permission#BLUETOOTH} permission.
 *
 * {@see BluetoothAdapter}
 * {@see BluetoothSocket}
 */
public final class IBluetoothDevice{

    /**
     * Sentinel error value for this class. Guaranteed to not equal any other
     * integer constant in this class. Provided as a convenience for functions
     * that require a sentinel error value, for example:
     * <p><code>Intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,
     * BluetoothDevice.ERROR)</code>
     */
    public static final int ERROR = Integer.MIN_VALUE;

    /**
     * Broadcast Action: Remote device discovered.
     * <p>Sent when a remote device is found during discovery.
     * <p>Always contains the extra fields {@link #EXTRA_DEVICE} and {@link
     * #EXTRA_CLASS}. Can contain the extra fields {@link #EXTRA_NAME} and/or
     * {@link #EXTRA_RSSI} if they are available.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
     // TODO: Change API to not broadcast RSSI if not available (incoming connection)
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_FOUND =
            "android.bluetooth.device.action.FOUND";

    /**
     * Broadcast Action: Remote device disappeared.
     * <p>Sent when a remote device that was found in the last discovery is not
     * found in the current discovery.
     * <p>Always contains the extra field {@link #EXTRA_DEVICE}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     * @hide
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_DISAPPEARED =
            "android.bluetooth.device.action.DISAPPEARED";

    /**
     * Broadcast Action: Bluetooth class of a remote device has changed.
     * <p>Always contains the extra fields {@link #EXTRA_DEVICE} and {@link
     * #EXTRA_CLASS}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     * @see {@link BluetoothClass}
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_CLASS_CHANGED =
            "android.bluetooth.device.action.CLASS_CHANGED";

    /**
     * Broadcast Action: Indicates a low level (ACL) connection has been
     * established with a remote device.
     * <p>Always contains the extra field {@link #EXTRA_DEVICE}.
     * <p>ACL connections are managed automatically by the Android Bluetooth
     * stack.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_ACL_CONNECTED =
            "android.bluetooth.device.action.ACL_CONNECTED";

    /**
     * Broadcast Action: Indicates that a low level (ACL) disconnection has
     * been requested for a remote device, and it will soon be disconnected.
     * <p>This is useful for graceful disconnection. Applications should use
     * this intent as a hint to immediately terminate higher level connections
     * (RFCOMM, L2CAP, or profile connections) to the remote device.
     * <p>Always contains the extra field {@link #EXTRA_DEVICE}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_ACL_DISCONNECT_REQUESTED =
            "android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED";

    /**
     * Broadcast Action: Indicates a low level (ACL) disconnection from a
     * remote device.
     * <p>Always contains the extra field {@link #EXTRA_DEVICE}.
     * <p>ACL connections are managed automatically by the Android Bluetooth
     * stack.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_ACL_DISCONNECTED =
            "android.bluetooth.device.action.ACL_DISCONNECTED";

    /**
     * Broadcast Action: Indicates the friendly name of a remote device has
     * been retrieved for the first time, or changed since the last retrieval.
     * <p>Always contains the extra fields {@link #EXTRA_DEVICE} and {@link
     * #EXTRA_NAME}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_NAME_CHANGED =
            "android.bluetooth.device.action.NAME_CHANGED";

    /**
     * Broadcast Action: Indicates a change in the bond state of a remote
     * device. For example, if a device is bonded (paired).
     * <p>Always contains the extra fields {@link #EXTRA_DEVICE}, {@link
     * #EXTRA_BOND_STATE} and {@link #EXTRA_PREVIOUS_BOND_STATE}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    // Note: When EXTRA_BOND_STATE is BOND_NONE then this will also
    // contain a hidden extra field EXTRA_REASON with the result code.
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_BOND_STATE_CHANGED =
            "android.bluetooth.device.action.BOND_STATE_CHANGED";

    /**
     * Used as a Parcelable {@link BluetoothDevice} extra field in every intent
     * broadcast by this class. It contains the {@link BluetoothDevice} that
     * the intent applies to.
     */
    public static final String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";

    /**
     * Used as a String extra field in {@link #ACTION_NAME_CHANGED} and {@link
     * #ACTION_FOUND} intents. It contains the friendly Bluetooth name.
     */
    public static final String EXTRA_NAME = "android.bluetooth.device.extra.NAME";

    /**
     * Used as an optional short extra field in {@link #ACTION_FOUND} intents.
     * Contains the RSSI value of the remote device as reported by the
     * Bluetooth hardware.
     */
    public static final String EXTRA_RSSI = "android.bluetooth.device.extra.RSSI";

    /**
     * Used as an Parcelable {@link BluetoothClass} extra field in {@link
     * #ACTION_FOUND} and {@link #ACTION_CLASS_CHANGED} intents.
     */
    public static final String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";

    /**
     * Used as an int extra field in {@link #ACTION_BOND_STATE_CHANGED} intents.
     * Contains the bond state of the remote device.
     * <p>Possible values are:
     * {@link #BOND_NONE},
     * {@link #BOND_BONDING},
     * {@link #BOND_BONDED}.
      */
    public static final String EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE";
    /**
     * Used as an int extra field in {@link #ACTION_BOND_STATE_CHANGED} intents.
     * Contains the previous bond state of the remote device.
     * <p>Possible values are:
     * {@link #BOND_NONE},
     * {@link #BOND_BONDING},
     * {@link #BOND_BONDED}.
      */
    public static final String EXTRA_PREVIOUS_BOND_STATE =
            "android.bluetooth.device.extra.PREVIOUS_BOND_STATE";
    /**
     * Indicates the remote device is not bonded (paired).
     * <p>There is no shared link key with the remote device, so communication
     * (if it is allowed at all) will be unauthenticated and unencrypted.
     */
    public static final int BOND_NONE = 10;
    /**
     * Indicates bonding (pairing) is in progress with the remote device.
     */
    public static final int BOND_BONDING = 11;
    /**
     * Indicates the remote device is bonded (paired).
     * <p>A shared link keys exists locally for the remote device, so
     * communication can be authenticated and encrypted.
     * <p><i>Being bonded (paired) with a remote device does not necessarily
     * mean the device is currently connected. It just means that the ponding
     * procedure was compeleted at some earlier time, and the link key is still
     * stored locally, ready to use on the next connection.
     * </i>
     */
    public static final int BOND_BONDED = 12;

    /** @hide */
    public static final String EXTRA_REASON = "android.bluetooth.device.extra.REASON";
    /** @hide */
    public static final String EXTRA_PAIRING_VARIANT =
            "android.bluetooth.device.extra.PAIRING_VARIANT";
    /** @hide */
    public static final String EXTRA_PASSKEY = "android.bluetooth.device.extra.PASSKEY";

    /**
     * Broadcast Action: This intent is used to broadcast the {@link UUID}
     * wrapped as a {@link android.os.ParcelUuid} of the remote device after it
     * has been fetched. This intent is sent only when the UUIDs of the remote
     * device are requested to be fetched using Service Discovery Protocol
     * <p> Always contains the extra field {@link #EXTRA_DEVICE}
     * <p> Always contains the extra filed {@link #EXTRA_UUID}
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     * @hide
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_UUID =
            "android.bleutooth.device.action.UUID";

    /**
     * Broadcast Action: Indicates a failure to retrieve the name of a remote
     * device.
     * <p>Always contains the extra field {@link #EXTRA_DEVICE}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     * @hide
     */
    //TODO: is this actually useful?
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_NAME_FAILED =
            "android.bluetooth.device.action.NAME_FAILED";

    /** @hide */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_PAIRING_REQUEST =
            "android.bluetooth.device.action.PAIRING_REQUEST";
    /** @hide */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_PAIRING_CANCEL =
            "android.bluetooth.device.action.PAIRING_CANCEL";

    /** A bond attempt succeeded
     * @hide */
    public static final int BOND_SUCCESS = 0;
    /** A bond attempt failed because pins did not match, or remote device did
     * not respond to pin request in time
     * @hide */
    public static final int UNBOND_REASON_AUTH_FAILED = 1;
    /** A bond attempt failed because the other side explicilty rejected
     * bonding
     * @hide */
    public static final int UNBOND_REASON_AUTH_REJECTED = 2;
    /** A bond attempt failed because we canceled the bonding process
     * @hide */
    public static final int UNBOND_REASON_AUTH_CANCELED = 3;
    /** A bond attempt failed because we could not contact the remote device
     * @hide */
    public static final int UNBOND_REASON_REMOTE_DEVICE_DOWN = 4;
    /** A bond attempt failed because a discovery is in progress
     * @hide */
    public static final int UNBOND_REASON_DISCOVERY_IN_PROGRESS = 5;
    /** A bond attempt failed because of authentication timeout
     * @hide */
    public static final int UNBOND_REASON_AUTH_TIMEOUT = 6;
    /** A bond attempt failed because of repeated attempts
     * @hide */
    public static final int UNBOND_REASON_REPEATED_ATTEMPTS = 7;
    /** A bond attempt failed because we received an Authentication Cancel
     *  by remote end
     * @hide */
    public static final int UNBOND_REASON_REMOTE_AUTH_CANCELED = 8;
    /** An existing bond was explicitly revoked
     * @hide */
    public static final int UNBOND_REASON_REMOVED = 9;

    /** The user will be prompted to enter a pin
     * @hide */
    public static final int PAIRING_VARIANT_PIN = 0;
    /** The user will be prompted to enter a passkey
     * @hide */
    public static final int PAIRING_VARIANT_PASSKEY = 1;
    /** The user will be prompted to confirm the passkey displayed on the screen
     * @hide */
    public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    /** The user will be prompted to accept or deny the incoming pairing request
     * @hide */
    public static final int PAIRING_VARIANT_CONSENT = 3;
    /** The user will be prompted to enter the passkey displayed on remote device
     * @hide */
    public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;

    /**
     * Used as an extra field in {@link #ACTION_UUID} intents,
     * Contains the {@link android.os.ParcelUuid}s of the remote device which
     * is a parcelable version of {@link UUID}.
     * @hide
     */
    public static final String EXTRA_UUID = "android.bluetooth.device.extra.UUID";

   

}
