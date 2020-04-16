/*
 * This file is part of RskJ
 * Copyright (C) 2020 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.net.light.message;

import co.rsk.net.light.LightClientMessageCodes;
import co.rsk.net.rlpx.LCMessageFactory;
import org.ethereum.core.BlockFactory;
import org.ethereum.crypto.HashUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GetAccountsMessageTest {

    private long id;
    private byte[] blockHash;
    private byte[] addressHash;

    @Before
    public void setUp() {
        id = 1;
        blockHash = HashUtil.randomHash();
        addressHash = HashUtil.randomHash();
    }

    @Test
    public void messageCreationShouldBeCorrect() {
        GetAccountsMessage testMessage = new GetAccountsMessage(id, blockHash, addressHash);
        assertEquals(LightClientMessageCodes.GET_ACCOUNTS, testMessage.getCommand());
        assertEquals(testMessage.getId(), id);
        assertArrayEquals(testMessage.getBlockHash(), blockHash);
        assertArrayEquals(testMessage.getAddressHash(), addressHash);
    }

    @Test
    public void messageEncodeDecodeShouldBeCorrect() {

        GetAccountsMessage testMessage = new GetAccountsMessage(id, blockHash, addressHash);
        byte[] encoded = testMessage.getEncoded();
        LCMessageFactory lcMessageFactory = new LCMessageFactory(mock(BlockFactory.class));
        byte code = LightClientMessageCodes.GET_ACCOUNTS.asByte();
        GetAccountsMessage message = (GetAccountsMessage) lcMessageFactory.create(code, encoded);

        assertEquals(testMessage.getId(), message.getId());
        assertArrayEquals(testMessage.getBlockHash(), message.getBlockHash());
        assertArrayEquals(testMessage.getAddressHash(), message.getAddressHash());
        assertEquals(testMessage.getCommand(), message.getCommand());
        assertEquals(testMessage.getAnswerMessage(), message.getAnswerMessage());
        assertArrayEquals(encoded, message.getEncoded());
    }

}