/*
*   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.ballerinalang.bre.bvm;

import org.ballerinalang.model.types.BField;
import org.ballerinalang.model.types.BStructureType;
import org.ballerinalang.model.types.BType;
import org.ballerinalang.model.types.TypeTags;
import org.ballerinalang.model.values.BBlob;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.model.values.StructureType;
import org.ballerinalang.util.codegen.ObjectTypeInfo;
import org.ballerinalang.util.codegen.StructureTypeInfo;
import org.ballerinalang.util.program.BLangFunctions;

/**
 * Util Class for handling structs in Ballerina VM.
 *
 * @since 0.89
 */
public class BLangVMStructs {

    /**
     * Create BStruct for given StructInfo and BValues.
     *
     * @param structInfo {@link StructureTypeInfo} of the BStruct
     * @param values     field values of the BStruct.
     * @return BStruct instance.
     */
    public static BStruct createBStruct(StructureTypeInfo structInfo, Object... values) {
        BStructureType structType = structInfo.getType();
        BStruct bStruct = new BStruct(structType);

        int[] indexes = new int[] {-1, -1, -1, -1, -1, -1};
        BField[] structFields = structType.getFields();
        for (int i = 0; i < structFields.length; i++) {
            if (values.length < i + 1) {
                break;
            }
            BType paramType = structFields[i].getFieldType();
            setValue(bStruct, indexes, paramType.getTag(), values[i]);
        }
        return bStruct;
    }

    /**
     * Create ballerina object.
     *
     * @param objectInfo {@link ObjectTypeInfo} of the BStruct
     * @param values     field values of the BStruct.
     * @return BStruct instance.
     */
    public static BStruct createObject(ObjectTypeInfo objectInfo, BValue... values) {
        BStructureType structType = objectInfo.getType();
        BStruct bStruct = new BStruct(structType);
        BValue[] vals = new BValue[values.length + 1];
        vals[0] = bStruct;
        System.arraycopy(values, 0, vals, 1, values.length);
        BLangFunctions.invokeCallable(objectInfo.initializer.functionInfo, vals);
        return bStruct;
    }

    @SuppressWarnings("rawtypes")
    private static void setValue(StructureType structureType, int[] regIndexes, int typeTag, Object value) {
        int index;
        switch (typeTag) {
            case TypeTags.INT_TAG:
                index = ++regIndexes[0];
                if (value != null) {
                    if (value instanceof Integer) {
                        structureType.setIntField(index, (Integer) value);
                    } else if (value instanceof Long) {
                        structureType.setIntField(index, (Long) value);
                    } else if (value instanceof BInteger) {
                        structureType.setIntField(index, ((BInteger) value).intValue());
                    }
                }
                break;
            case TypeTags.FLOAT_TAG:
                index = ++regIndexes[1];
                if (value != null) {
                    if (value instanceof Float) {
                        structureType.setFloatField(index, (Float) value);
                    } else if (value instanceof Double) {
                        structureType.setFloatField(index, (Double) value);
                    } else if (value instanceof BFloat) {
                        structureType.setFloatField(index, ((BFloat) value).floatValue());
                    }
                }
                break;
            case TypeTags.STRING_TAG:
                index = ++regIndexes[2];
                if (value != null) {
                    if (value instanceof String) {
                        structureType.setStringField(index, (String) value);
                    } else if (value instanceof BValue) {
                        structureType.setStringField(index, ((BValue) value).stringValue());
                    }
                }
                break;
            case TypeTags.BOOLEAN_TAG:
                index = ++regIndexes[3];
                if (value != null) {
                    if (value instanceof Boolean) {
                        structureType.setBooleanField(index, (Boolean) value ? 1 : 0);
                    } else if (value instanceof BBoolean) {
                        structureType.setBooleanField(index, ((BBoolean) value).booleanValue() ? 1 : 0);
                    }
                }
                break;
            case TypeTags.BLOB_TAG:
                index = ++regIndexes[4];
                if (value != null && value instanceof BBlob) {
                    structureType.setBlobField(index, ((BBlob) value).blobValue());
                }
                break;
            default:
                index = ++regIndexes[5];
                if (value != null && (value instanceof BRefType)) {
                    structureType.setRefField(index, (BRefType) value);
                }
        }
    }
}
