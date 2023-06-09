grammar Json;

json: value EOF;

value: jsonNumber
     | jsonString
     | jsonBoolean
     | jsonObject
     | jsonArray
     | NULL
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

jsonNumber: INT;

jsonString: STRING;

WS: [ \t\r\n]+ -> skip;

STRING: '"' [a-zA-Z0-9' ']+ '"';

INT: [0-9]+;

TRUE: 'true';

FALSE: 'false';

NULL: 'null';
