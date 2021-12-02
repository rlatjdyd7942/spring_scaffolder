SpringScaffolder
=
***
SpringScaffolder is a file generator to create basic CRUD api for _Spring_(__kotlin__)

This generates _Thymeleaf_, _Jpa_ based files

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
#### set table name
```shell
java -jar spring_scaffold.jar scaffold model Article title:String "description:String?" -t article_table_name
```
Use `-t` option to set `table_name`
#### model with additional package name
```shell
java -jar spring_scaffold.jar scaffold model article.Article title:String "description:String?"
```
### scaffold all
creates model, repository, controller, view templates
#### use like `scaffold model`
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?"
```
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -t article_table_name
```
```shell
java -jar spring_scaffold.jar scaffold all article.Article title:String "description:String?" -t article_table_name
```
#### add additional path to controller and view
```shell
java -jar spring_scaffold.jar scaffold all article.Article title:String "description:String?" -c path.to.controller
```
Use `-c` option to add additional path to `controller` and `view` files
#### exclude file generation
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -e "model"
```
Use `-e` option to exclude file generation. belows are keywords you can use to exclude
- model
- controller
- repository
- view
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -e "model,repository"
```
Use multiple keywords with comma `,`
#### etc
```shell
java -jar spring_scaffold.jar scaffold all Article title:String "description:String?" -b true
```
Use `-b true` to generate _bootstrap styled_ view files (i made it for my own use)