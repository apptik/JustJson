# JustJson

[![Build Status](https://travis-ci.org/apptik/JustJson.svg?branch=master)](https://travis-ci.org/apptik/JustJson)
[![Join the chat at https://gitter.im/apptik/JustJson](https://badges.gitter.im/apptik/JustJson.svg)](https://gitter.im/apptik/JustJson?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![StackExchange](https://img.shields.io/stackexchange/stackoverflow/t/JustJson.svg)](http://stackoverflow.com/questions/tagged/JustJson)


JSON helper library for Android and Java
----------------------------------------

JustJson is NOT a standard json2pojo library with multiple adapters.

It simply converts json string/stream to java Objects
* JsonObject - Iterable handy Map wrapper 
* JsonArray - List implementation
* JsonString - String wrapper
* JsonBoolean - boolean wrapper
* JsonNumber - number wrapper

which are then flexible & easy to read/modify/iterate/search/wrap.

It came out of the need for a dynamic, simple and lightweight Json parser for Android(unlike Jackson).
The code started as a mixture of https://android.googlesource.com/platform/libcore/+/master/json/ and the Json utils from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/util .

JustJson is super simple, super small and fast json lib.



It can be used on Android and Java in general.

## Download

Find [the latest JARs][mvn] or grab via Maven:
```xml
<dependency>
  <groupId>io.apptik.json</groupId>
  <artifactId>json-XXX</artifactId>
  <version>1.0.4</version>
</dependency>
```
or Gradle:
```groovy
compile 'io.apptik.json:json-XXX:1.0.4'
```

Downloads of the released versions are available in [Sonatype's `releases` repository][release].

Snapshots of the development versions are available in [Sonatype's `snapshots` repository][snap].

## Features
- Fast and easy Json Parser and Writer (comparable or better performance than Android Native, GSON and Jackson libraries)
- Flexible Json Wrapper helper implementations that hold json data and media type. Ideal for Model classes.
- Json-Schema validation 
- Android UI generation 

## Examples

TODO


## Questions

[StackOverflow with tag 'JustJson'](http://stackoverflow.com/questions/ask)

## Modules

* [Json Core][json-core] - the json core module responsible for mapping json string/stream to basic Java Objects

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.json/json-core.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.json/json-core)
[![VersionEye](https://www.versioneye.com/java/io.apptik.json:json-core/1.0.4/badge.svg)](https://www.versioneye.com/java/io.apptik.json:json-core/1.0.4)
* [Json Warpper][json-wrapper] - json wrapper classes that are used to wrap generic json representation around some defined interface

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.json/json-wrapper.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.json/json-wrapper)
[![VersionEye](https://www.versioneye.com/java/io.apptik.json:json-wrapper/1.0.4/badge.svg)](https://www.versioneye.com/java/io.apptik.json:json-wrapper/1.0.4)
* [Json Schema][json-schema] - helper to provide some json wrapper MetaInfo based on [json-schema][json-schema.org]

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.json/json-schema.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.json/json-schema)
[![VersionEye](https://www.versioneye.com/java/io.apptik.json:json-schema/1.0.4/badge.svg)](https://www.versioneye.com/java/io.apptik.json:json-schema/1.0.4)
* [Json Generator][json-generator] - Random customizable json generator what uses [json-schema][json-schema.org] metainfo

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.json/json-core.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.json/json-generator)
[![VersionEye](https://www.versioneye.com/java/io.apptik.json:json-core/1.0.4/badge.svg)](https://www.versioneye.com/java/io.apptik.json:json-generator/1.0.4)
* [Json AWS][json-aws] - helper for working with AWS json libraries

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.json/json-core.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.json/json-aws)
[![VersionEye](https://www.versioneye.com/java/io.apptik.json:json-core/1.0.4/badge.svg)](https://www.versioneye.com/java/io.apptik.json:json-aws/1.0.4)


## Licence

    Copyright (C) 2016 AppTik Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [mvn]: http://search.maven.org/#search|ga|1|io.apptik.json
 [release]: https://oss.sonatype.org/content/repositories/releases/io/apptik/json/
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/io/apptik/json/
 
 [json-core]: https://github.com/apptik/JustJson/tree/master/json-core
 [json-wrapper]: https://github.com/apptik/JustJson/tree/master/json-wrapper
 [json-schema]: https://github.com/apptik/JustJson/tree/master/json-schema
 [json-generator]: https://github.com/apptik/JustJson/tree/master/json-generator
 [json-aws]: https://github.com/apptik/JustJson/tree/master/json-aws
 [json-schema.org]: http://json-schema.org/



