/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package moja.global.reportingtables.exceptions;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class ServerException extends Exception {

	public ServerException() {
		super();
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

	protected ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
