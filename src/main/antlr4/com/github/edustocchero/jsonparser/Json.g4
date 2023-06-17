grammar Json;

json: value EOF;

value: jsonNumber
     | jsonString
     | jsonBoolean
     | jsonObject
     | jsonArray
     | jsonNull
     ;

jsonObject: '{' jsonPair (',' jsonPair)* '}'
          | '{' '}'
          ;

jsonPair: jsonString ':' value;

jsonArray: '[' ']'                    #emptyArray
         | '[' value (',' value)* ']' #someArray
         ;

jsonBoolean: TRUE  #booleanTrue
           | FALSE #booleanFalse
           ;

jsonNumber: NUMBER;

jsonString: STRING;

jsonNull: NULL;

WS: [ \t\r\n]+ -> skip;

STRING: '"' [a-zA-Z0-9' ']+ '"';

NUMBER: FLOAT (('e'|'E') FLOAT)?;
FLOAT: '-'? INT ('.' INT)?;
INT: [0-9]+;

TRUE: 'true';
FALSE: 'false';

NULL: 'null';
