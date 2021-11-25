SpringScaffolder
=
***
SpringScaffolder is a file generator to create basic CRUD api with _Spring_, _Thymeleaf_, _Jpa_ on __kotlin__.

Build
-
***
```shell
./gradlew build
```

Commands
-
with `spring_scaffold.jar` file
***
### scaffold init
```shell
java -jar spring_scaffold.jar scaffold init
```
`scaffold init` creates following files
- .scaffold.yml
- src/main/resources/templates/layout/layout.html
### scaffold model
creates model and repository
```shell
java -jar spring_scaffold.jar scaffold model Article title:String "description:String?"
```
```shell
java -jar spring_scaffold.jar scaffold model Article title:String "description:String?" -t article_table_name
```
```shell
java -jar spring_scaffold.jar scaffold model article.Article title:String "description:String?"
```
### scaffold all
creates model, repository, controller, view templates
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?"
```
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -t article_table_name
```
```shell
java -jar spring_scaffold.jar scaffold all article.Article title:String "description:String?" -t article_table_name
```
```shell
java -jar spring_scaffold.jar scaffold all article.Article title:String "description:String?" -c path.to.controller
```
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -e "model, repository"
```