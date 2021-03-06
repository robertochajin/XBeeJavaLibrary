/**
 * Copyright 2017, Digi International Inc.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.digi.xbee.api.packet.ip;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digi.xbee.api.IPDevice;
import com.digi.xbee.api.models.IPProtocol;
import com.digi.xbee.api.packet.APIFrameType;
import com.digi.xbee.api.packet.ip.RXIPv4Packet;
import com.digi.xbee.api.utils.HexUtils;

@PrepareForTest({Inet4Address.class, RXIPv4Packet.class})
@RunWith(PowerMockRunner.class)
public class RXIPv4PacketTest {

	// Constants.
	private static final String IP_ADDRESS = "10.10.11.12";

	// Variables.
	private int frameType = APIFrameType.RX_IPV4.getValue();
	private Inet4Address sourceAddress;
	private Inet4Address sourceAddressBroadcast;
	private int destPort = 0x0025;
	private int sourcePort = 0x00B3;
	private IPProtocol protocol = IPProtocol.TCP;
	private int status = 0x00;
	private byte[] data = "Test".getBytes();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	public RXIPv4PacketTest() throws Exception {
		sourceAddress = (Inet4Address) Inet4Address.getByName(IP_ADDRESS);
		sourceAddressBroadcast = (Inet4Address) Inet4Address.getByName(IPDevice.BROADCAST_IP);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>A {@code NullPointerException} exception must be thrown when parsing a
	 * {@code null} byte array.</p>
	 */
	@Test
	public final void testCreatePacketNullPayload() {
		// Set up the resources for the test.
		byte[] payload = null;
		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("RX IPv4 packet payload cannot be null.")));

		// Call the method under test that should throw a NullPointerException.
		RXIPv4Packet.createPacket(payload);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>An {@code IllegalArgumentException} exception must be thrown when
	 * parsing an empty byte array.</p>
	 */
	@Test
	public final void testCreatePacketEmptyPayload() {
		// Set up the resources for the test.
		byte[] payload = new byte[0];
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Incomplete RX IPv4 packet.")));

		// Call the method under test that should throw an IllegalArgumentException.
		RXIPv4Packet.createPacket(payload);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>An {@code IllegalArgumentException} exception must be thrown when
	 * parsing a byte array shorter than the needed one is provided.</p>
	 */
	@Test
	public final void testCreatePacketPayloadShorterThanNeeded() {
		// Set up the resources for the test.
		byte[] payload = new byte[10];
		payload[0] = (byte)frameType;
		System.arraycopy(sourceAddress.getAddress(), 0, payload, 1, sourceAddress.getAddress().length);
		payload[5] = (byte)(destPort >> 8);
		payload[6] = (byte)destPort;
		payload[7] = (byte)(sourcePort >> 8);
		payload[8] = (byte)sourcePort;
		payload[9] = (byte)protocol.getID();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Incomplete RX IPv4 packet.")));

		// Call the method under test that should throw an IllegalArgumentException.
		RXIPv4Packet.createPacket(payload);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>An {@code IllegalArgumentException} exception must be thrown when
	 * parsing a byte array not including the Frame type.</p>
	 */
	@Test
	public final void testCreatePacketPayloadNotIncludingFrameType() {
		// Set up the resources for the test.
		byte[] payload = new byte[9 + data.length];
		System.arraycopy(sourceAddress.getAddress(), 0, payload, 0, sourceAddress.getAddress().length);
		payload[4] = (byte)(destPort >> 8);
		payload[5] = (byte)destPort;
		payload[6] = (byte)(sourcePort >> 8);
		payload[7] = (byte)sourcePort;
		payload[8] = (byte)protocol.getID();
		System.arraycopy(data, 0, payload, 9, data.length);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Payload is not a RX IPv4 packet.")));

		// Call the method under test that should throw an IllegalArgumentException.
		RXIPv4Packet.createPacket(payload);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>An {@code IllegalArgumentException} exception must be thrown when
	 * parsing a byte array with an invalid IP address.</p>
	 */
	@Test
	public final void testCreatePacketPayloadInvalidIP() throws Exception {
		// Set up the resources for the test.
		byte[] payload = new byte[11];
		payload[0] = (byte)frameType;
		System.arraycopy(sourceAddress.getAddress(), 0, payload, 1, sourceAddress.getAddress().length);
		payload[5] = (byte)(destPort >> 8);
		payload[6] = (byte)destPort;
		payload[7] = (byte)(sourcePort >> 8);
		payload[8] = (byte)sourcePort;
		payload[9] = (byte)protocol.getID();
		payload[10] = (byte)status;

		PowerMockito.mockStatic(Inet4Address.class);
		PowerMockito.when(Inet4Address.getByAddress(Mockito.any(byte[].class))).thenThrow(new UnknownHostException());

		exception.expect(IllegalArgumentException.class);

		// Call the method under test that should throw an IllegalArgumentException.
		RXIPv4Packet.createPacket(payload);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>A valid API RX IPv4 packet with the provided options without data
	 * is created.</p>
	 */
	@Test
	public final void testCreatePacketValidPayloadWithoutData() {
		// Set up the resources for the test.
		byte[] payload = new byte[11];
		payload[0] = (byte)frameType;
		System.arraycopy(sourceAddress.getAddress(), 0, payload, 1, sourceAddress.getAddress().length);
		payload[5] = (byte)(destPort >> 8);
		payload[6] = (byte)destPort;
		payload[7] = (byte)(sourcePort >> 8);
		payload[8] = (byte)sourcePort;
		payload[9] = (byte)protocol.getID();
		payload[10] = (byte)status;

		// Call the method under test.
		RXIPv4Packet packet = RXIPv4Packet.createPacket(payload);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(payload.length)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddress)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(nullValue()));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));

		assertThat("Returned payload array is not the expected one", packet.getPacketData(), is(equalTo(payload)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>A valid API IPv4 IPv4 packet with the provided options and data is
	 * created.</p>
	 */
	@Test
	public final void testCreatePacketValidPayloadWithData() {
		// Set up the resources for the test.
		byte[] payload = new byte[11 + data.length];
		payload[0] = (byte)frameType;
		System.arraycopy(sourceAddress.getAddress(), 0, payload, 1, sourceAddress.getAddress().length);
		payload[5] = (byte)(destPort >> 8);
		payload[6] = (byte)destPort;
		payload[7] = (byte)(sourcePort >> 8);
		payload[8] = (byte)sourcePort;
		payload[9] = (byte)protocol.getID();
		payload[10] = (byte)status;
		System.arraycopy(data, 0, payload, 11, data.length);

		// Call the method under test.
		RXIPv4Packet packet = RXIPv4Packet.createPacket(payload);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(payload.length)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddress)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(equalTo(data)));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));

		assertThat("Returned payload array is not the expected one", packet.getPacketData(), is(equalTo(payload)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#createPacket(byte[])}.
	 *
	 * <p>A valid API IPv4 IPv4 packet with the provided options and data is
	 * created (broadcast transmission).</p>
	 */
	@Test
	public final void testCreatePacketValidPayloadWithDataBroadcast() {
		// Set up the resources for the test.
		byte[] payload = new byte[11 + data.length];
		payload[0] = (byte)frameType;
		System.arraycopy(sourceAddressBroadcast.getAddress(), 0, payload, 1, sourceAddressBroadcast.getAddress().length);
		payload[5] = (byte)(destPort >> 8);
		payload[6] = (byte)destPort;
		payload[7] = (byte)(sourcePort >> 8);
		payload[8] = (byte)sourcePort;
		payload[9] = (byte)protocol.getID();
		payload[10] = (byte)status;
		System.arraycopy(data, 0, payload, 11, data.length);

		// Call the method under test.
		RXIPv4Packet packet = RXIPv4Packet.createPacket(payload);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(payload.length)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddressBroadcast)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(equalTo(data)));
		assertThat("Packet is not broadcast", packet.isBroadcast(), is(equalTo(true)));

		assertThat("Returned payload array is not the expected one", packet.getPacketData(), is(equalTo(payload)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a null destination address. This
	 * must throw a {@code NullPointerException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketDestAddressNull() {
		// Set up the resources for the test.
		sourceAddress = null;

		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("Source address cannot be null.")));

		// Call the method under test that should throw a NullPointerException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a negative destination port. This
	 * must throw an {@code IllegalArgumentException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketDestPortNegative() {
		// Set up the resources for the test.
		destPort = -6;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a destination port bigger than
	 * 65535. This must throw an {@code IllegalArgumentException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketDestPortBigger() {
		// Set up the resources for the test.
		destPort = 66200;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a negative source port. This
	 * must throw an {@code IllegalArgumentException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketSourcePortNegative() {
		// Set up the resources for the test.
		sourcePort = -6;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a source port bigger than
	 * 65535. This must throw an {@code IllegalArgumentException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketSourcePortBigger() {
		// Set up the resources for the test.
		sourcePort = 66200;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with a null protocol. This must throw
	 * a {@code NullPointerException}.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketProtocolNull() {
		// Set up the resources for the test.
		protocol = null;

		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("Protocol cannot be null.")));

		// Call the method under test that should throw a NullPointerException.
		new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet without data ({@code null}).</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketValidDataNull() {
		// Set up the resources for the test.
		data = null;

		int expectedLength = 11;

		// Call the method under test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(expectedLength)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddress)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(nullValue()));
		assertThat("RX IPv4 packet needs API Frame ID", packet.needsAPIFrameID(), is(equalTo(false)));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with data.</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketValidDataNotNull() {
		// Set up the resources for the test.
		int expectedLength = 11 + data.length;

		// Call the method under test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(expectedLength)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddress)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(data));
		assertThat("RX IPv4 packet needs API Frame ID", packet.needsAPIFrameID(), is(equalTo(false)));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#RXIPv4Packet(Inet4Address, int, int, IPProtocol, byte[])}.
	 *
	 * <p>Construct a new RX IPv4 packet with data (broadcast transmission).</p>
	 */
	@Test
	public final void testCreateRXIPv4PacketValidDataNotNullBroadcast() {
		// Set up the resources for the test.
		int expectedLength = 11 + data.length;

		// Call the method under test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddressBroadcast, destPort, sourcePort, protocol, data);

		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(expectedLength)));
		assertThat("Returned dest address is not the expected one", packet.getSourceAddress(), is(equalTo(sourceAddressBroadcast)));
		assertThat("Returned dest port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned source port is not the expected one", packet.getDestPort(), is(equalTo(destPort)));
		assertThat("Returned protocol is not the expected one", packet.getProtocol(), is(equalTo(protocol)));
		assertThat("Returned data is not the expected one", packet.getData(), is(data));
		assertThat("RX IPv4 packet needs API Frame ID", packet.needsAPIFrameID(), is(equalTo(false)));
		assertThat("Packet is not broadcast", packet.isBroadcast(), is(equalTo(true)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getAPIData()}.
	 *
	 * <p>Test the get API parameters with a {@code null} received data.</p>
	 */
	@Test
	public final void testGetAPIDataReceivedDataNull() {
		// Set up the resources for the test.
		data = null;

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		int expectedLength = 10;
		byte[] expectedData = new byte[expectedLength];
		System.arraycopy(sourceAddress.getAddress(), 0, expectedData, 0, sourceAddress.getAddress().length);
		expectedData[4] = (byte)(destPort >> 8);
		expectedData[5] = (byte)destPort;
		expectedData[6] = (byte)(sourcePort >> 8);
		expectedData[7] = (byte)sourcePort;
		expectedData[8] = (byte)protocol.getID();

		// Call the method under test.
		byte[] apiData = packet.getAPIData();

		// Verify the result.
		assertThat("API data is not the expected", apiData, is(equalTo(expectedData)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getAPIData()}.
	 *
	 * <p>Test the get API parameters with a not-{@code null} received data.</p>
	 */
	@Test
	public final void testGetAPIDataReceivedDataNotNull() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		int expectedLength = 10 + data.length;
		byte[] expectedData = new byte[expectedLength];
		System.arraycopy(sourceAddress.getAddress(), 0, expectedData, 0, sourceAddress.getAddress().length);
		expectedData[4] = (byte)(destPort >> 8);
		expectedData[5] = (byte)destPort;
		expectedData[6] = (byte)(sourcePort >> 8);
		expectedData[7] = (byte)sourcePort;
		expectedData[8] = (byte)protocol.getID();
		System.arraycopy(data, 0, expectedData, 10, data.length);

		// Call the method under test.
		byte[] apiData = packet.getAPIData();

		// Verify the result.
		assertThat("API data is not the expected", apiData, is(equalTo(expectedData)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getAPIPacketParameters()}.
	 *
	 * <p>Test the get API parameters but with a {@code null} received data.</p>
	 */
	@Test
	public final void testGetAPIPacketParametersReceivedDataNull() {
		// Set up the resources for the test.
		data = null;

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		LinkedHashMap<String, String> packetParams = packet.getAPIPacketParameters();

		// Verify the result.
		assertThat("Packet parameters map size is not the expected one", packetParams.size(), is(equalTo(5)));
		assertThat("Returned source address is not the expected one", packetParams.get("Source address"), is(equalTo(HexUtils.prettyHexString(sourceAddress.getAddress()) + " (" + sourceAddress.getHostAddress() + ")")));
		assertThat("Returned dest port is not the expected one", packetParams.get("Destination port"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(destPort, 2)) + " (" + destPort + ")")));
		assertThat("Returned source port is not the expected one", packetParams.get("Source port"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(sourcePort, 2)) + " (" + sourcePort + ")")));
		assertThat("Returned protocol is not the expected one", packetParams.get("Protocol"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(protocol.getID(), 1)) + " (" + protocol.getName() + ")")));
		assertThat("Returned status is not the expected one", packetParams.get("Status"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(0x00, 1)) + " (Reserved)")));
		assertThat("Data is not the expected", packetParams.get("Data"), is(nullValue(String.class)));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getAPIPacketParameters()}.
	 *
	 * <p>Test the get API parameters but with a not-{@code null} received data.</p>
	 */
	@Test
	public final void testGetAPIPacketParametersReceivedDataNotNull() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		LinkedHashMap<String, String> packetParams = packet.getAPIPacketParameters();

		// Verify the result.
		assertThat("Packet parameters map size is not the expected one", packetParams.size(), is(equalTo(6)));
		assertThat("Returned source address is not the expected one", packetParams.get("Source address"), is(equalTo(HexUtils.prettyHexString(sourceAddress.getAddress()) + " (" + sourceAddress.getHostAddress() + ")")));
		assertThat("Returned dest port is not the expected one", packetParams.get("Destination port"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(destPort, 2)) + " (" + destPort + ")")));
		assertThat("Returned source port is not the expected one", packetParams.get("Source port"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(sourcePort, 2)) + " (" + sourcePort + ")")));
		assertThat("Returned protocol is not the expected one", packetParams.get("Protocol"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(protocol.getID(), 1)) + " (" + protocol.getName() + ")")));
		assertThat("Returned status is not the expected one", packetParams.get("Status"), is(equalTo(HexUtils.prettyHexString(HexUtils.integerToHexString(0x00, 1)) + " (Reserved)")));
		assertThat("Data is not the expected", packetParams.get("Data"), is(equalTo(HexUtils.prettyHexString(HexUtils.byteArrayToHexString(data)))));
		assertThat("Packet is broadcast", packet.isBroadcast(), is(equalTo(false)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setDestAddress(Inet4Address)}.
	 */
	@Test
	public final void testSetDestAddressNull() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("Source address cannot be null.")));

		// Call the method under test that should throw a NullPointerException.
		packet.setSourceAddress(null);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setDestAddress(Inet4Address)}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testSetDestAddressNotNull() throws Exception {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		Inet4Address newAddress = (Inet4Address) Inet4Address.getByName("192.168.1.30");

		// Call the method under test.
		packet.setSourceAddress(newAddress);

		// Verify the result.
		assertThat("Dest address is not the expected one", packet.getSourceAddress(), is(equalTo(newAddress)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setDestPort(int)}.
	 */
	@Test
	public final void testSetDestPortNegative() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		packet.setDestPort(-1);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setDestPort(int)}.
	 */
	@Test
	public final void testSetDestPortBigger() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		packet.setDestPort(65536);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setDestPort(int)}.
	 */
	@Test
	public final void testSetDestPortValid() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		int newPort = 0;

		// Call the method under test.
		packet.setDestPort(newPort);

		// Verify the result.
		assertThat("Dest port is not the expected one", packet.getDestPort(), is(equalTo(newPort)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setSourcePort(int)}.
	 */
	@Test
	public final void testSetSourcePortNegative() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		packet.setSourcePort(-1);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setSourcePort(int)}.
	 */
	@Test
	public final void testSetSourcePortBigger() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Port must be between 0 and 65535.")));

		// Call the method under test that should throw an IllegalArgumentException.
		packet.setSourcePort(65536);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setSourcePort(int)}.
	 */
	@Test
	public final void testSetSourcePortValid() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		int newPort = 65535;

		// Call the method under test.
		packet.setSourcePort(newPort);

		// Verify the result.
		assertThat("Source port is not the expected one", packet.getSourcePort(), is(equalTo(newPort)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setProtocol(IPProtocol)}.
	 */
	@Test
	public final void testSetProtocolNull() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("Protocol cannot be null.")));

		// Call the method under test that should throw a NullPointerException.
		packet.setProtocol(null);
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setProtocol(IPProtocol)}.
	 */
	@Test
	public final void testSetProtocolNotNull() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		IPProtocol newProtocol = IPProtocol.TCP_SSL;

		// Call the method under test.
		packet.setProtocol(newProtocol);

		// Verify the result.
		assertThat("Protocol is not the expected one", packet.getProtocol(), is(equalTo(newProtocol)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getData()}.
	 */
	@Test
	public final void testGetDataNullData() {
		// Set up the resources for the test.
		data = null;

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		byte[] result = packet.getData();

		// Verify the result.
		assertThat("Data must be the same", result, is(equalTo(data)));
		assertThat("Data must be null", result, is(nullValue(byte[].class)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#getData()}.
	 */
	@Test
	public final void testGetDataValidData() {
		// Set up the resources for the test.
		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		byte[] result = packet.getData();

		// Verify the result.
		assertThat("Data must be the same", result, is(equalTo(data)));
		assertThat("Data must not be the same object", result.hashCode(), is(not(equalTo(data.hashCode()))));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setData(byte[])}.
	 */
	@Test
	public final void testSetDataNullData() {
		// Set up the resources for the test.
		byte[] newData = null;

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		packet.setData(newData);

		byte[] result = packet.getData();

		// Verify the result.
		assertThat("Data must be the same", result, is(equalTo(newData)));
		assertThat("Data must be null", result, is(nullValue(byte[].class)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setData(byte[])}.
	 */
	@Test
	public final void testSetDataValidData() {
		// Set up the resources for the test.
		byte[] newData = "New data".getBytes();

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		packet.setData(newData);

		byte[] result = packet.getData();

		// Verify the result.
		assertThat("Data must be the same", result, is(equalTo(newData)));
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.ip.RXIPv4Packet#setData(byte[])}.
	 */
	@Test
	public final void testSetDataAndModifyOriginal() {
		// Set up the resources for the test.
		byte[] newData = "New data".getBytes();

		RXIPv4Packet packet = new RXIPv4Packet(sourceAddress, destPort, sourcePort, protocol, data);

		// Call the method under test.
		packet.setData(newData);

		byte[] backup = Arrays.copyOf(newData, newData.length);
		newData[0] = 0x00;

		byte[] result = packet.getData();

		// Verify the result.
		assertThat("Data must be the same as the setted data", result, is(equalTo(backup)));
		assertThat("Data must not be the current value of received data", result, is(not(equalTo(data))));
		assertThat("Data must not be the same object", result.hashCode(), is(not(equalTo(backup.hashCode()))));
		assertThat("Data must not be the same object", result.hashCode(), is(not(equalTo(data.hashCode()))));
	}
}
