# simple_data_query

## Description:
A simple service able to store and retrieve data.

## How to run?
To use command line interface run "Main.java",  
It will add to the database items from 'input.json' and output results to 'output.json' by default

Data structure is pre-defined as following:

| Fields                                        | Example     | 
| -----------                                   | ----------- | 
| id - text <br>title - text<br> content - text <br>views - integer number<br>timestamp - integer number|{ "id": "first-post", "title": "My First Post","content": "Hello World!","views": 1,"timestamp": 1555832341 }


### API requirements
API consists of two end-points - one to store data and one to retrieve it.

| Endpoint                                        | Example     | Response  | 
| -----------                                     | ----------- |-----------|
| Query<br> Takes query as input and returns matching entries. Query format is defined below.| query("EQUAL(id,"abc")") | [{"id":"abc","title":"Alphabet","content":"A, B, C, ...","views":1,"timestamp":1555832341}]     |
| Store<br> Take entity and stores it. ID must remain unique. If record with given ID already exists, it should be overwritten.| store({"id":"abc","title":"Alphabet","content":"A, B, C, ...","views":1,"timestamp":1555832341}) | |

### Query
| Operator                                        | Example     | 
| -----------                                     | ----------- |
| EQUAL(property,value) <br> Filters only values which have matching property value.| EQUAL(id,"first-post") <br> EQUAL(views,100)   |
| GREATER_THAN(property,value) <br> Filters only values for which property is greater than the given value. Valid only for number values.| GREATER_THAN(views,100) | 
| LESS_THAN(property,value) <br> Filters only values for which property is less than the given value. Valid only for number values.| LESS_THAN(views,100) |
| AND(a,b) <br> Filters only values for which both a and b are true.| AND(EQUAL(id,"first-post"),EQUAL(views,100)) | 
| OR(a,b) <br> Filters only values for which either a or b is true (or both).| OR(EQUAL(id,"first-post"),EQUAL(id,"second-post")) | 
| NOT(a) <br> Filters only values for which a is false.| NOT(EQUAL(id,"first-post")) |
| BETWEEN(property,value,value) * NEW <br> Filters only values for which property is between the given value. Valid only for number values.| BETWEEN(views,20,100) | 
| UPDATE(id,property,value) * NEW <br> Update an item with a new property value| UPDATE("first-post",views,500) | 
| DELETE(id) * NEW <br> Delete an item from database| DELETE("first-post") |
| BIGGEST(property) * NEW <br> Filters only values for which property is the largest Valid only for number property | BIGGEST(views) |
| SMALLEST(property) * NEW <br> Filters only values for which property is the largest Valid only for number property | SMALLEST(views) |


## Troubleshooting
If you get trouble to maven test the project, please try to reload all maven project, via maven plugin on your IDE.
or from terminal: <br> `mvn clean install -U`

If you still get error running maven, in the format of:
`Transfer failed for <REPO_MAVEN_APACHE_URL> Received fatal alert: protocol_version`
Try to add the following lines to your pom.xml: <br>
` <properties>  
<maven.compiler.source>1.8</maven.compiler.source>
<maven.compiler.target>1.8</maven.compiler.target>  
</properties>`  