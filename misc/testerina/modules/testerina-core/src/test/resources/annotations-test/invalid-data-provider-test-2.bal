import ballerina.test;
import ballerina.io;

@test:config{
    dataProvider:"invalidDataGen"
}
function testFunc2 (string fValue, string sValue, string result) {

    var value1, _ = <int>fValue;
    var value2, _ = <int>sValue;
    var result1, _ = <int>result;
    io:println("Input params: ["+fValue+","+sValue+","+result+"]");
    test:assertEquals(value1 + value2, result1, "The sum is not correct");
}

function invalidDataGen()(string[][], string) {
    return [["1", "2", "3"]], "hi";
}
