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
package com.digi.xbee.api.exceptions;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InvalidPacketExceptionTest {

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
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException()}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionDefault() {
		// Setup the resources for the test.
		String expectedMessage = "The XBee API packet is not properly formed.";
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException();
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(expectedMessage)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(nullValue(Throwable.class)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String)}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionMessage() {
		// Setup the resources for the test.
		String message = "This is the message";
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(nullValue(Throwable.class)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String)}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionMessageNull() {
		// Setup the resources for the test.
		String message = null;
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(nullValue(Throwable.class)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String, Throwable)}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionMessageAndCause() {
		// Setup the resources for the test.
		String message = "This is the message";
		Throwable cause = new Exception();
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message, cause);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(equalTo(cause)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String, Throwable)}.
	 */
	@Test
	public final void testInvalidPacketExceptionMessageNullAndCause() {
		// Setup the resources for the test.
		String message = null;
		Throwable cause = new Exception();
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message, cause);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(equalTo(cause)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String, Throwable)}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionMessageAndCauseNull() {
		// Setup the resources for the test.
		String message = "This is the message";
		Throwable cause = null;
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message, cause);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(equalTo(cause)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.exceptions.InvalidPacketException#InvalidPacketException(String, Throwable)}.
	 */
	@Test
	public final void testCreateInvalidPacketExceptionMessageAndCauseBothNull() {
		// Setup the resources for the test.
		String message = null;
		Throwable cause = null;
		
		// Call the method under test.
		InvalidPacketException e = new InvalidPacketException(message, cause);
		
		// Verify the result.
		assertThat("Created 'InvalidPacketException' does not have the expected message", 
				e.getMessage(), is(equalTo(message)));
		assertThat("Created 'InvalidPacketException' does not have the expected cause", 
				e.getCause(), is(equalTo(cause)));
	}
}
