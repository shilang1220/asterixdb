/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.asterix.runtime.functions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.asterix.common.utils.CodeGenHelper;
import org.apache.asterix.om.functions.IFunctionCollection;
import org.apache.asterix.om.functions.IFunctionDescriptorFactory;
import org.apache.asterix.om.functions.IFunctionRegistrant;
import org.apache.asterix.runtime.aggregates.collections.FirstElementAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.collections.LastElementAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.collections.ListifyAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.collections.LocalFirstElementAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.collections.NullWriterAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarAvgDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarCountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarCountDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarKurtosisDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarMaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarMaxDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarMinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarMinDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSkewnessDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlAvgDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlCountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlCountDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlKurtosisDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlMaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlMaxDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlMinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlMinDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlSkewnessDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlStddevDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlStddevPopDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlSumDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlVarDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSqlVarPopDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarStddevDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarStddevPopDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarSumDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarVarDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.scalar.ScalarVarPopDistinctAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableCountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableGlobalVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableIntermediateVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableLocalVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlCountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.serializable.std.SerializableVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.AvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.CountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.GlobalVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.IntermediateVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.KurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalMaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalMinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSamplingAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlMaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlMinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.LocalVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.MaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.MinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.RangeMapAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlAvgAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlCountAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlKurtosisAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlMaxAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlMinAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlSkewnessAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlStddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlStddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlSumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlVarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SqlVarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.StddevAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.StddevPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.SumAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.VarAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.std.VarPopAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.stream.EmptyStreamAggregateDescriptor;
import org.apache.asterix.runtime.aggregates.stream.NonEmptyStreamAggregateDescriptor;
import org.apache.asterix.runtime.evaluators.accessors.CircleCenterAccessor;
import org.apache.asterix.runtime.evaluators.accessors.CircleRadiusAccessor;
import org.apache.asterix.runtime.evaluators.accessors.LineRectanglePolygonAccessor;
import org.apache.asterix.runtime.evaluators.accessors.PointXCoordinateAccessor;
import org.apache.asterix.runtime.evaluators.accessors.PointYCoordinateAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalDayAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalHourAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalEndAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalEndDateAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalEndDatetimeAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalEndTimeAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalStartAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalStartDateAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalStartDatetimeAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalIntervalStartTimeAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalMillisecondAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalMinuteAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalMonthAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalSecondAccessor;
import org.apache.asterix.runtime.evaluators.accessors.TemporalYearAccessor;
import org.apache.asterix.runtime.evaluators.comparisons.EqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.GreaterThanDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.GreaterThanOrEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.LessThanDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.LessThanOrEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.MissingIfEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.NanIfEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.NegInfIfEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.NotEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.NullIfEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.comparisons.PosInfIfEqualsDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ABinaryBase64StringConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ABinaryHexStringConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ABooleanConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ACircleConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ADateConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ADateTimeConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ADayTimeDurationConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ADoubleConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ADurationConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AFloatConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AInt16ConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AInt32ConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AInt64ConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AInt8ConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AIntervalConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AIntervalStartFromDateConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AIntervalStartFromDateTimeConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AIntervalStartFromTimeConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ALineConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.APoint3DConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.APointConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.APolygonConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ARectangleConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AStringConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ATimeConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AUUIDFromStringConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.AYearMonthDurationConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.ClosedRecordConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.OpenRecordConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.OrderedListConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.constructors.UnorderedListConstructorDescriptor;
import org.apache.asterix.runtime.evaluators.functions.AndDescriptor;
import org.apache.asterix.runtime.evaluators.functions.AnyCollectionMemberDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayAppendDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayConcatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayContainsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayDistinctDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayFlattenDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayIfNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayInsertDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayIntersectDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayPositionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayPrependDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayPutDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayRangeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayRemoveDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayRepeatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayReplaceDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayReverseDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArraySliceWithEndPositionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArraySliceWithoutEndPositionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArraySortDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayStarDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArraySymDiffDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArraySymDiffnDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ArrayUnionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CastTypeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CastTypeLaxDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CheckUnknownDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CodePointToStringDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateCircleDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateLineDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateMBRDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreatePointDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreatePolygonDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateQueryUIDDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateRectangleDescriptor;
import org.apache.asterix.runtime.evaluators.functions.CreateUUIDDescriptor;
import org.apache.asterix.runtime.evaluators.functions.DeepEqualityDescriptor;
import org.apache.asterix.runtime.evaluators.functions.FullTextContainsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.FullTextContainsWithoutOptionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.GetItemDescriptor;
import org.apache.asterix.runtime.evaluators.functions.GetJobParameterByNameDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfInfDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfMissingDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfMissingOrNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfNanDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfNanOrInfDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IfSystemNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.InjectFailureDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsArrayDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsAtomicDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsBooleanDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsMissingDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsNumberDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsNumericAddCompatibleDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsObjectDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsStringDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsSystemNullDescriptor;
import org.apache.asterix.runtime.evaluators.functions.IsUnknownDescriptor;
import org.apache.asterix.runtime.evaluators.functions.LenDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NotDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericACosDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericASinDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericATan2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericATanDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericAbsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericAddDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericCeilingDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericCosDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericCoshDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericDegreesDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericDivDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericDivideDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericExpDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericFloorDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericLnDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericLogDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericModuloDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericMultiplyDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericPowerDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericRadiansDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericRoundDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericRoundHalfToEven2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericRoundHalfToEvenDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericSignDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericSinDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericSinhDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericSqrtDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericSubDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericTanDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericTanhDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericTruncDescriptor;
import org.apache.asterix.runtime.evaluators.functions.NumericUnaryMinusDescriptor;
import org.apache.asterix.runtime.evaluators.functions.OrDescriptor;
import org.apache.asterix.runtime.evaluators.functions.RandomDescriptor;
import org.apache.asterix.runtime.evaluators.functions.RandomWithSeedDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SleepDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SpatialAreaDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SpatialCellDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SpatialDistanceDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringConcatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringContainsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringEndsWithDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringEqualDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringInitCapDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringJoinDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringLTrim2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.StringLTrimDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringLengthDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringLikeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringLowerCaseDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringPositionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRTrim2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRTrimDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpContainsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpContainsWithFlagDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpLikeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpLikeWithFlagDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpPositionDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpPositionWithFlagDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpReplaceDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRegExpReplaceWithFlagDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringRepeatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringReplaceDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringReplaceWithLimitDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringReverseDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringSplitDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringStartsWithDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringToCodePointDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringTrim2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.StringTrimDescriptor;
import org.apache.asterix.runtime.evaluators.functions.StringUpperCaseDescriptor;
import org.apache.asterix.runtime.evaluators.functions.Substring2Descriptor;
import org.apache.asterix.runtime.evaluators.functions.SubstringAfterDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SubstringBeforeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SubstringDescriptor;
import org.apache.asterix.runtime.evaluators.functions.SwitchCaseDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToArrayDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToAtomicDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToBigIntDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToBooleanDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToDoubleDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToNumberDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToObjectDescriptor;
import org.apache.asterix.runtime.evaluators.functions.ToStringDescriptor;
import org.apache.asterix.runtime.evaluators.functions.TreatAsIntegerDescriptor;
import org.apache.asterix.runtime.evaluators.functions.UUIDDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.BinaryConcatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.BinaryLengthDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.FindBinaryDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.FindBinaryFromDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.ParseBinaryDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.PrintBinaryDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.SubBinaryFromDescriptor;
import org.apache.asterix.runtime.evaluators.functions.binary.SubBinaryFromToDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.FieldAccessByIndexDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.FieldAccessByNameDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.FieldAccessNestedDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.GetRecordFieldValueDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.GetRecordFieldsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.PairsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordAddDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordAddFieldsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordConcatDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordConcatStrictDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordLengthDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordMergeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordNamesDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordPairsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordPutDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordRemoveDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordRemoveFieldsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordRenameDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordReplaceDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordUnwrapDescriptor;
import org.apache.asterix.runtime.evaluators.functions.records.RecordValuesDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.AdjustDateTimeForTimeZoneDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.AdjustTimeForTimeZoneDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.CalendarDuartionFromDateDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.CalendarDurationFromDateTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.CurrentDateDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.CurrentDateTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.CurrentTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DateFromDatetimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DateFromUnixTimeInDaysDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DatetimeFromDateAndTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DatetimeFromUnixTimeInMsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DatetimeFromUnixTimeInSecsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DayOfWeekDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DayTimeDurationGreaterThanComparatorDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DayTimeDurationLessThanComparatorDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DurationEqualDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DurationFromIntervalDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DurationFromMillisecondsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.DurationFromMonthsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.GetDayTimeDurationDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.GetOverlappingIntervalDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.GetYearMonthDurationDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalAfterDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalBeforeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalBinDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalCoveredByDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalCoversDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalEndedByDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalEndsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalMeetsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalMetByDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalOverlappedByDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalOverlapsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalStartedByDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.IntervalStartsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.MillisecondsFromDayTimeDurationDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.MonthsFromYearMonthDurationDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.OverlapBinsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.OverlapDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.ParseDateDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.ParseDateTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.ParseTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.PrintDateDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.PrintDateTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.PrintTimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.TimeFromDatetimeDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.TimeFromUnixTimeInMsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.UnixTimeFromDateInDaysDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.UnixTimeFromDatetimeInMsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.UnixTimeFromDatetimeInSecsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.UnixTimeFromTimeInMsDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.YearMonthDurationGreaterThanComparatorDescriptor;
import org.apache.asterix.runtime.evaluators.functions.temporal.YearMonthDurationLessThanComparatorDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.DenseRankRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.NtileRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.PercentRankRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.RankRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.RowNumberRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.TidRunningAggregateDescriptor;
import org.apache.asterix.runtime.runningaggregates.std.WinPartitionLenRunningAggregateDescriptor;
import org.apache.asterix.runtime.unnestingfunctions.std.RangeDescriptor;
import org.apache.asterix.runtime.unnestingfunctions.std.ScanCollectionDescriptor;
import org.apache.asterix.runtime.unnestingfunctions.std.SubsetCollectionDescriptor;

/**
 * This class holds a list of function descriptor factories.
 */
public final class FunctionCollection implements IFunctionCollection {
    private static final long serialVersionUID = -8308873930697425307L;

    private static final String FACTORY = "FACTORY";

    private final ArrayList<IFunctionDescriptorFactory> descriptorFactories = new ArrayList<>();

    @Override
    public void add(IFunctionDescriptorFactory descriptorFactory) {
        descriptorFactories.add(descriptorFactory);
    }

    @Override
    public void addGenerated(IFunctionDescriptorFactory descriptorFactory) {
        add(getGeneratedFunctionDescriptorFactory(descriptorFactory.createFunctionDescriptor().getClass()));
    }

    public static FunctionCollection createDefaultFunctionCollection() {
        FunctionCollection fc = new FunctionCollection();

        // array functions
        fc.add(ArrayRemoveDescriptor.FACTORY);
        fc.add(ArrayPutDescriptor.FACTORY);
        fc.add(ArrayPrependDescriptor.FACTORY);
        fc.add(ArrayAppendDescriptor.FACTORY);
        fc.add(ArrayInsertDescriptor.FACTORY);
        fc.add(ArrayPositionDescriptor.FACTORY);
        fc.add(ArrayRepeatDescriptor.FACTORY);
        fc.add(ArrayContainsDescriptor.FACTORY);
        fc.add(ArrayReverseDescriptor.FACTORY);
        fc.add(ArraySortDescriptor.FACTORY);
        fc.add(ArrayDistinctDescriptor.FACTORY);
        fc.add(ArrayUnionDescriptor.FACTORY);
        fc.add(ArrayIntersectDescriptor.FACTORY);
        fc.add(ArrayIfNullDescriptor.FACTORY);
        fc.add(ArrayConcatDescriptor.FACTORY);
        fc.add(ArrayRangeDescriptor.FACTORY);
        fc.add(ArrayFlattenDescriptor.FACTORY);
        fc.add(ArrayReplaceDescriptor.FACTORY);
        fc.add(ArraySliceWithEndPositionDescriptor.FACTORY);
        fc.add(ArraySliceWithoutEndPositionDescriptor.FACTORY);
        fc.add(ArraySymDiffDescriptor.FACTORY);
        fc.add(ArraySymDiffnDescriptor.FACTORY);
        fc.add(ArrayStarDescriptor.FACTORY);

        // unnesting functions
        fc.add(TidRunningAggregateDescriptor.FACTORY);
        fc.add(ScanCollectionDescriptor.FACTORY);
        fc.add(RangeDescriptor.FACTORY);
        fc.add(SubsetCollectionDescriptor.FACTORY);

        // aggregate functions
        fc.add(ListifyAggregateDescriptor.FACTORY);
        fc.add(CountAggregateDescriptor.FACTORY);
        fc.add(AvgAggregateDescriptor.FACTORY);
        fc.add(LocalAvgAggregateDescriptor.FACTORY);
        fc.add(IntermediateAvgAggregateDescriptor.FACTORY);
        fc.add(GlobalAvgAggregateDescriptor.FACTORY);
        fc.add(SumAggregateDescriptor.FACTORY);
        fc.add(LocalSumAggregateDescriptor.FACTORY);
        fc.add(IntermediateSumAggregateDescriptor.FACTORY);
        fc.add(GlobalSumAggregateDescriptor.FACTORY);
        fc.add(MaxAggregateDescriptor.FACTORY);
        fc.add(LocalMaxAggregateDescriptor.FACTORY);
        fc.add(MinAggregateDescriptor.FACTORY);
        fc.add(LocalMinAggregateDescriptor.FACTORY);
        fc.add(FirstElementAggregateDescriptor.FACTORY);
        fc.add(LocalFirstElementAggregateDescriptor.FACTORY);
        fc.add(LastElementAggregateDescriptor.FACTORY);
        fc.add(StddevAggregateDescriptor.FACTORY);
        fc.add(LocalStddevAggregateDescriptor.FACTORY);
        fc.add(IntermediateStddevAggregateDescriptor.FACTORY);
        fc.add(GlobalStddevAggregateDescriptor.FACTORY);
        fc.add(LocalSamplingAggregateDescriptor.FACTORY);
        fc.add(RangeMapAggregateDescriptor.FACTORY);
        fc.add(StddevPopAggregateDescriptor.FACTORY);
        fc.add(LocalStddevPopAggregateDescriptor.FACTORY);
        fc.add(IntermediateStddevPopAggregateDescriptor.FACTORY);
        fc.add(GlobalStddevPopAggregateDescriptor.FACTORY);
        fc.add(VarAggregateDescriptor.FACTORY);
        fc.add(LocalVarAggregateDescriptor.FACTORY);
        fc.add(IntermediateVarAggregateDescriptor.FACTORY);
        fc.add(GlobalVarAggregateDescriptor.FACTORY);
        fc.add(VarPopAggregateDescriptor.FACTORY);
        fc.add(LocalVarPopAggregateDescriptor.FACTORY);
        fc.add(IntermediateVarPopAggregateDescriptor.FACTORY);
        fc.add(GlobalVarPopAggregateDescriptor.FACTORY);
        fc.add(KurtosisAggregateDescriptor.FACTORY);
        fc.add(LocalKurtosisAggregateDescriptor.FACTORY);
        fc.add(IntermediateKurtosisAggregateDescriptor.FACTORY);
        fc.add(GlobalKurtosisAggregateDescriptor.FACTORY);
        fc.add(SkewnessAggregateDescriptor.FACTORY);
        fc.add(LocalSkewnessAggregateDescriptor.FACTORY);
        fc.add(IntermediateSkewnessAggregateDescriptor.FACTORY);
        fc.add(GlobalSkewnessAggregateDescriptor.FACTORY);
        fc.add(EmptyStreamAggregateDescriptor.FACTORY);
        fc.add(NonEmptyStreamAggregateDescriptor.FACTORY);
        fc.add(NullWriterAggregateDescriptor.FACTORY);

        // serializable aggregates
        fc.add(SerializableCountAggregateDescriptor.FACTORY);
        fc.add(SerializableAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableSumAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSumAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSumAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSumAggregateDescriptor.FACTORY);
        fc.add(SerializableStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableVarAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalVarAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateVarAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalVarAggregateDescriptor.FACTORY);
        fc.add(SerializableVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSkewnessAggregateDescriptor.FACTORY);

        // scalar aggregates
        fc.add(ScalarCountAggregateDescriptor.FACTORY);
        fc.add(ScalarCountDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarAvgAggregateDescriptor.FACTORY);
        fc.add(ScalarAvgDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSumAggregateDescriptor.FACTORY);
        fc.add(ScalarSumDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarMaxAggregateDescriptor.FACTORY);
        fc.add(ScalarMaxDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarMinAggregateDescriptor.FACTORY);
        fc.add(ScalarMinDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarStddevAggregateDescriptor.FACTORY);
        fc.add(ScalarStddevDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarStddevPopAggregateDescriptor.FACTORY);
        fc.add(ScalarStddevPopDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarVarAggregateDescriptor.FACTORY);
        fc.add(ScalarVarDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarVarPopAggregateDescriptor.FACTORY);
        fc.add(ScalarVarPopDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarKurtosisAggregateDescriptor.FACTORY);
        fc.add(ScalarKurtosisDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSkewnessAggregateDescriptor.FACTORY);
        fc.add(ScalarSkewnessDistinctAggregateDescriptor.FACTORY);

        // SQL aggregates
        fc.add(SqlCountAggregateDescriptor.FACTORY);
        fc.add(SqlAvgAggregateDescriptor.FACTORY);
        fc.add(LocalSqlAvgAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlAvgAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlAvgAggregateDescriptor.FACTORY);
        fc.add(SqlSumAggregateDescriptor.FACTORY);
        fc.add(LocalSqlSumAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlSumAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlSumAggregateDescriptor.FACTORY);
        fc.add(SqlMaxAggregateDescriptor.FACTORY);
        fc.add(LocalSqlMaxAggregateDescriptor.FACTORY);
        fc.add(SqlMinAggregateDescriptor.FACTORY);
        fc.add(LocalSqlMinAggregateDescriptor.FACTORY);
        fc.add(SqlStddevAggregateDescriptor.FACTORY);
        fc.add(LocalSqlStddevAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlStddevAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlStddevAggregateDescriptor.FACTORY);
        fc.add(SqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(LocalSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(SqlVarAggregateDescriptor.FACTORY);
        fc.add(LocalSqlVarAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlVarAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlVarAggregateDescriptor.FACTORY);
        fc.add(SqlVarPopAggregateDescriptor.FACTORY);
        fc.add(LocalSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(SqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(LocalSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(SqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(LocalSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(IntermediateSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(GlobalSqlSkewnessAggregateDescriptor.FACTORY);

        // SQL serializable aggregates
        fc.add(SerializableSqlCountAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlAvgAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlSumAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlSumAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlSumAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlSumAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlStddevAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlVarAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlVarAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlVarAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlVarAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(SerializableSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableLocalSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableIntermediateSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(SerializableGlobalSqlSkewnessAggregateDescriptor.FACTORY);

        // SQL scalar aggregates
        fc.add(ScalarSqlCountAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlCountDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlAvgAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlAvgDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlSumAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlSumDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlMaxAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlMaxDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlMinAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlMinDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlStddevAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlStddevDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlStddevPopAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlStddevPopDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlVarAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlVarDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlVarPopAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlVarPopDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlKurtosisAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlKurtosisDistinctAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlSkewnessAggregateDescriptor.FACTORY);
        fc.add(ScalarSqlSkewnessDistinctAggregateDescriptor.FACTORY);

        // window functions
        fc.add(DenseRankRunningAggregateDescriptor.FACTORY);
        fc.add(NtileRunningAggregateDescriptor.FACTORY);
        fc.add(RankRunningAggregateDescriptor.FACTORY);
        fc.add(RowNumberRunningAggregateDescriptor.FACTORY);
        fc.add(PercentRankRunningAggregateDescriptor.FACTORY);
        fc.add(WinPartitionLenRunningAggregateDescriptor.FACTORY);

        // boolean functions
        fc.add(AndDescriptor.FACTORY);
        fc.add(OrDescriptor.FACTORY);

        // Record constructors / functions
        fc.add(ClosedRecordConstructorDescriptor.FACTORY);
        fc.add(OpenRecordConstructorDescriptor.FACTORY);
        fc.add(RecordConcatDescriptor.FACTORY);
        fc.add(RecordConcatStrictDescriptor.FACTORY);

        // List constructors
        fc.add(OrderedListConstructorDescriptor.FACTORY);
        fc.add(UnorderedListConstructorDescriptor.FACTORY);

        // Sleep function
        fc.add(SleepDescriptor.FACTORY);

        // Inject failure function
        fc.add(InjectFailureDescriptor.FACTORY);

        // Get Job Parameter function
        fc.add(GetJobParameterByNameDescriptor.FACTORY);

        // Switch case
        fc.add(SwitchCaseDescriptor.FACTORY);

        // null functions
        fc.add(IsMissingDescriptor.FACTORY);
        fc.add(IsNullDescriptor.FACTORY);
        fc.add(IsUnknownDescriptor.FACTORY);
        fc.add(IsSystemNullDescriptor.FACTORY);
        fc.add(CheckUnknownDescriptor.FACTORY);
        fc.add(IfMissingDescriptor.FACTORY);
        fc.add(IfNullDescriptor.FACTORY);
        fc.add(IfMissingOrNullDescriptor.FACTORY);
        fc.add(IfSystemNullDescriptor.FACTORY);

        // uuid generators (zero independent functions)
        fc.add(CreateUUIDDescriptor.FACTORY);
        fc.add(UUIDDescriptor.FACTORY);
        fc.add(CreateQueryUIDDescriptor.FACTORY);
        fc.add(RandomDescriptor.FACTORY);
        fc.add(CurrentDateDescriptor.FACTORY);
        fc.add(CurrentTimeDescriptor.FACTORY);
        fc.add(CurrentDateTimeDescriptor.FACTORY);

        fc.add(IsNumericAddCompatibleDescriptor.FACTORY);

        // functions that need generated class for null-handling.

        // Element accessors.
        fc.add(FieldAccessByIndexDescriptor.FACTORY);
        fc.add(FieldAccessByNameDescriptor.FACTORY);
        fc.add(FieldAccessNestedDescriptor.FACTORY);

        fc.add(AnyCollectionMemberDescriptor.FACTORY);
        fc.add(GetItemDescriptor.FACTORY);

        // Numeric functions
        fc.add(IfInfDescriptor.FACTORY);
        fc.add(IfNanDescriptor.FACTORY);
        fc.add(IfNanOrInfDescriptor.FACTORY);
        fc.add(NumericUnaryMinusDescriptor.FACTORY);
        fc.add(NumericAddDescriptor.FACTORY);
        fc.add(NumericDivideDescriptor.FACTORY);
        fc.add(NumericDivDescriptor.FACTORY);
        fc.add(NumericMultiplyDescriptor.FACTORY);
        fc.add(NumericSubDescriptor.FACTORY);
        fc.add(NumericModuloDescriptor.FACTORY);
        fc.add(NumericPowerDescriptor.FACTORY);
        fc.add(NotDescriptor.FACTORY);
        fc.add(LenDescriptor.FACTORY);
        fc.add(NumericAbsDescriptor.FACTORY);
        fc.add(NumericCeilingDescriptor.FACTORY);
        fc.add(NumericFloorDescriptor.FACTORY);
        fc.add(NumericRoundDescriptor.FACTORY);
        fc.add(NumericRoundHalfToEvenDescriptor.FACTORY);
        fc.add(NumericRoundHalfToEven2Descriptor.FACTORY);
        fc.add(NumericACosDescriptor.FACTORY);
        fc.add(NumericASinDescriptor.FACTORY);
        fc.add(NumericATanDescriptor.FACTORY);
        fc.add(NumericDegreesDescriptor.FACTORY);
        fc.add(NumericRadiansDescriptor.FACTORY);
        fc.add(NumericCosDescriptor.FACTORY);
        fc.add(NumericCoshDescriptor.FACTORY);
        fc.add(NumericSinDescriptor.FACTORY);
        fc.add(NumericSinhDescriptor.FACTORY);
        fc.add(NumericTanDescriptor.FACTORY);
        fc.add(NumericTanhDescriptor.FACTORY);
        fc.add(NumericExpDescriptor.FACTORY);
        fc.add(NumericLnDescriptor.FACTORY);
        fc.add(NumericLogDescriptor.FACTORY);
        fc.add(NumericSqrtDescriptor.FACTORY);
        fc.add(NumericSignDescriptor.FACTORY);
        fc.add(NumericTruncDescriptor.FACTORY);
        fc.add(NumericATan2Descriptor.FACTORY);

        // Comparisons.
        fc.add(EqualsDescriptor.FACTORY);
        fc.add(GreaterThanDescriptor.FACTORY);
        fc.add(GreaterThanOrEqualsDescriptor.FACTORY);
        fc.add(LessThanDescriptor.FACTORY);
        fc.add(LessThanOrEqualsDescriptor.FACTORY);
        fc.add(NotEqualsDescriptor.FACTORY);

        // If-Equals functions
        fc.add(MissingIfEqualsDescriptor.FACTORY);
        fc.add(NullIfEqualsDescriptor.FACTORY);
        fc.add(NanIfEqualsDescriptor.FACTORY);
        fc.add(PosInfIfEqualsDescriptor.FACTORY);
        fc.add(NegInfIfEqualsDescriptor.FACTORY);

        // Binary functions
        fc.add(BinaryLengthDescriptor.FACTORY);
        fc.add(ParseBinaryDescriptor.FACTORY);
        fc.add(PrintBinaryDescriptor.FACTORY);
        fc.add(BinaryConcatDescriptor.FACTORY);
        fc.add(SubBinaryFromDescriptor.FACTORY);
        fc.add(SubBinaryFromToDescriptor.FACTORY);
        fc.add(FindBinaryDescriptor.FACTORY);
        fc.add(FindBinaryFromDescriptor.FACTORY);

        // String functions
        fc.add(StringLikeDescriptor.FACTORY);
        fc.add(StringContainsDescriptor.FACTORY);
        fc.add(StringEndsWithDescriptor.FACTORY);
        fc.add(StringStartsWithDescriptor.FACTORY);
        fc.add(SubstringDescriptor.FACTORY);
        fc.add(StringEqualDescriptor.FACTORY);
        fc.add(StringLowerCaseDescriptor.FACTORY);
        fc.add(StringUpperCaseDescriptor.FACTORY);
        fc.add(StringLengthDescriptor.FACTORY);
        fc.add(Substring2Descriptor.FACTORY);
        fc.add(SubstringBeforeDescriptor.FACTORY);
        fc.add(SubstringAfterDescriptor.FACTORY);
        fc.add(StringToCodePointDescriptor.FACTORY);
        fc.add(CodePointToStringDescriptor.FACTORY);
        fc.add(StringConcatDescriptor.FACTORY);
        fc.add(StringJoinDescriptor.FACTORY);
        fc.add(StringRegExpContainsDescriptor.FACTORY);
        fc.add(StringRegExpContainsWithFlagDescriptor.FACTORY);
        fc.add(StringRegExpLikeDescriptor.FACTORY);
        fc.add(StringRegExpLikeWithFlagDescriptor.FACTORY);
        fc.add(StringRegExpPositionDescriptor.FACTORY);
        fc.add(StringRegExpPositionWithFlagDescriptor.FACTORY);
        fc.add(StringRegExpReplaceDescriptor.FACTORY);
        fc.add(StringRegExpReplaceWithFlagDescriptor.FACTORY);
        fc.add(StringInitCapDescriptor.FACTORY);
        fc.add(StringTrimDescriptor.FACTORY);
        fc.add(StringLTrimDescriptor.FACTORY);
        fc.add(StringRTrimDescriptor.FACTORY);
        fc.add(StringTrim2Descriptor.FACTORY);
        fc.add(StringLTrim2Descriptor.FACTORY);
        fc.add(StringRTrim2Descriptor.FACTORY);
        fc.add(StringPositionDescriptor.FACTORY);
        fc.add(StringRepeatDescriptor.FACTORY);
        fc.add(StringReplaceDescriptor.FACTORY);
        fc.add(StringReplaceWithLimitDescriptor.FACTORY);
        fc.add(StringReverseDescriptor.FACTORY);
        fc.add(StringSplitDescriptor.FACTORY);

        // Constructors
        fc.add(ABooleanConstructorDescriptor.FACTORY);
        fc.add(ABinaryHexStringConstructorDescriptor.FACTORY);
        fc.add(ABinaryBase64StringConstructorDescriptor.FACTORY);
        fc.add(AStringConstructorDescriptor.FACTORY);
        fc.add(AInt8ConstructorDescriptor.FACTORY);
        fc.add(AInt16ConstructorDescriptor.FACTORY);
        fc.add(AInt32ConstructorDescriptor.FACTORY);
        fc.add(AInt64ConstructorDescriptor.FACTORY);
        fc.add(AFloatConstructorDescriptor.FACTORY);
        fc.add(ADoubleConstructorDescriptor.FACTORY);
        fc.add(APointConstructorDescriptor.FACTORY);
        fc.add(APoint3DConstructorDescriptor.FACTORY);
        fc.add(ALineConstructorDescriptor.FACTORY);
        fc.add(APolygonConstructorDescriptor.FACTORY);
        fc.add(ACircleConstructorDescriptor.FACTORY);
        fc.add(ARectangleConstructorDescriptor.FACTORY);
        fc.add(ATimeConstructorDescriptor.FACTORY);
        fc.add(ADateConstructorDescriptor.FACTORY);
        fc.add(ADateTimeConstructorDescriptor.FACTORY);
        fc.add(ADurationConstructorDescriptor.FACTORY);
        fc.add(AYearMonthDurationConstructorDescriptor.FACTORY);
        fc.add(ADayTimeDurationConstructorDescriptor.FACTORY);
        fc.add(AUUIDFromStringConstructorDescriptor.FACTORY);
        fc.add(AIntervalConstructorDescriptor.FACTORY);
        fc.add(AIntervalStartFromDateConstructorDescriptor.FACTORY);
        fc.add(AIntervalStartFromDateTimeConstructorDescriptor.FACTORY);
        fc.add(AIntervalStartFromTimeConstructorDescriptor.FACTORY);

        // Spatial
        fc.add(CreatePointDescriptor.FACTORY);
        fc.add(CreateLineDescriptor.FACTORY);
        fc.add(CreatePolygonDescriptor.FACTORY);
        fc.add(CreateCircleDescriptor.FACTORY);
        fc.add(CreateRectangleDescriptor.FACTORY);
        fc.add(SpatialAreaDescriptor.FACTORY);
        fc.add(SpatialDistanceDescriptor.FACTORY);
        fc.add(CreateMBRDescriptor.FACTORY);
        fc.add(SpatialCellDescriptor.FACTORY);
        fc.add(PointXCoordinateAccessor.FACTORY);
        fc.add(PointYCoordinateAccessor.FACTORY);
        fc.add(CircleRadiusAccessor.FACTORY);
        fc.add(CircleCenterAccessor.FACTORY);
        fc.add(LineRectanglePolygonAccessor.FACTORY);

        // full-text function
        fc.add(FullTextContainsDescriptor.FACTORY);
        fc.add(FullTextContainsWithoutOptionDescriptor.FACTORY);

        // Record functions.
        fc.add(GetRecordFieldsDescriptor.FACTORY);
        fc.add(GetRecordFieldValueDescriptor.FACTORY);
        fc.add(DeepEqualityDescriptor.FACTORY);
        fc.add(RecordMergeDescriptor.FACTORY);
        fc.add(RecordAddFieldsDescriptor.FACTORY);
        fc.add(RecordRemoveFieldsDescriptor.FACTORY);
        fc.add(RecordLengthDescriptor.FACTORY);
        fc.add(RecordNamesDescriptor.FACTORY);
        fc.add(RecordRemoveDescriptor.FACTORY);
        fc.add(RecordRenameDescriptor.FACTORY);
        fc.add(RecordUnwrapDescriptor.FACTORY);
        fc.add(RecordReplaceDescriptor.FACTORY);
        fc.add(RecordAddDescriptor.FACTORY);
        fc.add(RecordPutDescriptor.FACTORY);
        fc.add(RecordValuesDescriptor.FACTORY);
        fc.add(PairsDescriptor.FACTORY);

        // Spatial and temporal type accessors
        fc.add(TemporalYearAccessor.FACTORY);
        fc.add(TemporalMonthAccessor.FACTORY);
        fc.add(TemporalDayAccessor.FACTORY);
        fc.add(TemporalHourAccessor.FACTORY);
        fc.add(TemporalMinuteAccessor.FACTORY);
        fc.add(TemporalSecondAccessor.FACTORY);
        fc.add(TemporalMillisecondAccessor.FACTORY);
        fc.add(TemporalIntervalStartAccessor.FACTORY);
        fc.add(TemporalIntervalEndAccessor.FACTORY);
        fc.add(TemporalIntervalStartDateAccessor.FACTORY);
        fc.add(TemporalIntervalEndDateAccessor.FACTORY);
        fc.add(TemporalIntervalStartTimeAccessor.FACTORY);
        fc.add(TemporalIntervalEndTimeAccessor.FACTORY);
        fc.add(TemporalIntervalStartDatetimeAccessor.FACTORY);
        fc.add(TemporalIntervalEndDatetimeAccessor.FACTORY);

        // Temporal functions
        fc.add(UnixTimeFromDateInDaysDescriptor.FACTORY);
        fc.add(UnixTimeFromTimeInMsDescriptor.FACTORY);
        fc.add(UnixTimeFromDatetimeInMsDescriptor.FACTORY);
        fc.add(UnixTimeFromDatetimeInSecsDescriptor.FACTORY);
        fc.add(DateFromUnixTimeInDaysDescriptor.FACTORY);
        fc.add(DateFromDatetimeDescriptor.FACTORY);
        fc.add(TimeFromUnixTimeInMsDescriptor.FACTORY);
        fc.add(TimeFromDatetimeDescriptor.FACTORY);
        fc.add(DatetimeFromUnixTimeInMsDescriptor.FACTORY);
        fc.add(DatetimeFromUnixTimeInSecsDescriptor.FACTORY);
        fc.add(DatetimeFromDateAndTimeDescriptor.FACTORY);
        fc.add(CalendarDurationFromDateTimeDescriptor.FACTORY);
        fc.add(CalendarDuartionFromDateDescriptor.FACTORY);
        fc.add(AdjustDateTimeForTimeZoneDescriptor.FACTORY);
        fc.add(AdjustTimeForTimeZoneDescriptor.FACTORY);
        fc.add(IntervalBeforeDescriptor.FACTORY);
        fc.add(IntervalAfterDescriptor.FACTORY);
        fc.add(IntervalMeetsDescriptor.FACTORY);
        fc.add(IntervalMetByDescriptor.FACTORY);
        fc.add(IntervalOverlapsDescriptor.FACTORY);
        fc.add(IntervalOverlappedByDescriptor.FACTORY);
        fc.add(OverlapDescriptor.FACTORY);
        fc.add(IntervalStartsDescriptor.FACTORY);
        fc.add(IntervalStartedByDescriptor.FACTORY);
        fc.add(IntervalCoversDescriptor.FACTORY);
        fc.add(IntervalCoveredByDescriptor.FACTORY);
        fc.add(IntervalEndsDescriptor.FACTORY);
        fc.add(IntervalEndedByDescriptor.FACTORY);
        fc.add(DurationFromMillisecondsDescriptor.FACTORY);
        fc.add(DurationFromMonthsDescriptor.FACTORY);
        fc.add(YearMonthDurationGreaterThanComparatorDescriptor.FACTORY);
        fc.add(YearMonthDurationLessThanComparatorDescriptor.FACTORY);
        fc.add(DayTimeDurationGreaterThanComparatorDescriptor.FACTORY);
        fc.add(DayTimeDurationLessThanComparatorDescriptor.FACTORY);
        fc.add(MonthsFromYearMonthDurationDescriptor.FACTORY);
        fc.add(MillisecondsFromDayTimeDurationDescriptor.FACTORY);
        fc.add(DurationEqualDescriptor.FACTORY);
        fc.add(GetYearMonthDurationDescriptor.FACTORY);
        fc.add(GetDayTimeDurationDescriptor.FACTORY);
        fc.add(IntervalBinDescriptor.FACTORY);
        fc.add(OverlapBinsDescriptor.FACTORY);
        fc.add(DayOfWeekDescriptor.FACTORY);
        fc.add(ParseDateDescriptor.FACTORY);
        fc.add(ParseTimeDescriptor.FACTORY);
        fc.add(ParseDateTimeDescriptor.FACTORY);
        fc.add(PrintDateDescriptor.FACTORY);
        fc.add(PrintTimeDescriptor.FACTORY);
        fc.add(PrintDateTimeDescriptor.FACTORY);
        fc.add(GetOverlappingIntervalDescriptor.FACTORY);
        fc.add(DurationFromIntervalDescriptor.FACTORY);

        // Type functions.
        fc.add(IsArrayDescriptor.FACTORY);
        fc.add(IsAtomicDescriptor.FACTORY);
        fc.add(IsBooleanDescriptor.FACTORY);
        fc.add(IsNumberDescriptor.FACTORY);
        fc.add(IsObjectDescriptor.FACTORY);
        fc.add(IsStringDescriptor.FACTORY);
        fc.add(ToArrayDescriptor.FACTORY);
        fc.add(ToAtomicDescriptor.FACTORY);
        fc.add(ToBigIntDescriptor.FACTORY);
        fc.add(ToBooleanDescriptor.FACTORY);
        fc.add(ToDoubleDescriptor.FACTORY);
        fc.add(ToNumberDescriptor.FACTORY);
        fc.add(ToObjectDescriptor.FACTORY);
        fc.add(ToStringDescriptor.FACTORY);

        fc.add(TreatAsIntegerDescriptor.FACTORY);

        // Cast function
        fc.add(CastTypeDescriptor.FACTORY);
        fc.add(CastTypeLaxDescriptor.FACTORY);

        // Record function
        fc.add(RecordPairsDescriptor.FACTORY);

        // Other functions
        fc.add(RandomWithSeedDescriptor.FACTORY);

        ServiceLoader.load(IFunctionRegistrant.class).iterator().forEachRemaining(c -> c.register(fc));
        return fc;
    }

    public List<IFunctionDescriptorFactory> getFunctionDescriptorFactories() {
        return descriptorFactories;
    }

    /**
     * Gets the generated function descriptor factory from an <code>IFunctionDescriptor</code>
     * implementation class.
     *
     * @param cl,
     *            the class of an <code>IFunctionDescriptor</code> implementation.
     * @return the IFunctionDescriptorFactory instance defined in the class.
     */
    private static IFunctionDescriptorFactory getGeneratedFunctionDescriptorFactory(Class<?> cl) {
        try {
            String className =
                    CodeGenHelper.getGeneratedClassName(cl.getName(), CodeGenHelper.DEFAULT_SUFFIX_FOR_GENERATED_CLASS);
            Class<?> generatedCl = cl.getClassLoader().loadClass(className);
            Field factory = generatedCl.getDeclaredField(FACTORY);
            return (IFunctionDescriptorFactory) factory.get(null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
