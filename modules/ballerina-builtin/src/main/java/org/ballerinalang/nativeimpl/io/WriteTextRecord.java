/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.nativeimpl.io;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.io.channels.BTextRecordChannel;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;

/**
 * Native function ballerina.io#readCharacters
 *
 * @since 0.90
 */
@BallerinaFunction(
        packageName = "ballerina.io",
        functionName = "writeTextRecord",
        args = {@Argument(name = "channel", type = TypeKind.STRUCT),
                @Argument(name = "content", type = TypeKind.ARRAY, elementType = TypeKind.STRING)},
        isPublic = true
)
public class WriteTextRecord extends AbstractNativeFunction {

    /**
     * Writes records for a given file
     * {@inheritDoc}
     */
    @Override
    public BValue[] execute(Context context) {
        BStruct channel;
        BStringArray content;

        try {
            channel = (BStruct) getRefArgument(context, 0);
            content = (BStringArray) getRefArgument(context, 1);


            BTextRecordChannel textRecordChannel = (BTextRecordChannel) channel.getNativeData(IOConstants
                    .TXT_RECORD_CHANNEL_NAME);

            textRecordChannel.write(content);

        } catch (Throwable e) {
            String message = "Error occurred while writing text record .";
            throw new BallerinaException(message + e.getMessage(), context);
        }

        return VOID_RETURN;
    }
}
