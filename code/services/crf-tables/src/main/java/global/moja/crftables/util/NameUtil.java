/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.util;

import com.google.common.io.BaseEncoding;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 *
 * @author tonni
 */
public class NameUtil {
    
    public static String generateUniqueName(){
        return encodeUUIDBase64(UUID.randomUUID());
    }

    //See: https://stackoverflow.com/questions/4267475/generating-8-character-only-uuids
    private static String encodeUUIDBase64(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return StringUtils.trimTrailingCharacter(BaseEncoding.base64Url().encode(bb.array()), '=');
    }
}
