- [Serialization and De-serialization in Java](#serialization-and-de-serialization-in-java)
  - [Introduction](#introduction)
  - [Serializable and Externalizable in Java](#serializable-and-externalizable-in-java)
  - [Compatible and In-compatible changes in Java serialization](#compatible-and-in-compatible-changes-in-java-serialization)
  - [Data serialization in Redisson](#data-serialization-in-redisson)

### Serialization and De-serialization in Java

#### Introduction

+ Serialization is a mechanism of converting the state of an `object` into a `byte stream`. 

+ Deserialization is the reverse process where the `byte stream` is used to recreate the actual `Java object` in memory. 

+ This mechanism is used to `persist the object.`

![x](image/serialize-deserialize-java.png)

+ `Advantages` to Serialization
  + To `save/persist` state of an object
  + To `travel` an object across the network

![x](image/advantages-of-serialization.jpg)    

+ `SerialVersionUID`
  + The serialization runtime associates a `version number` with each serializable class called a SerialVersionUID, which is used during de-serialization to `verify` that `sender and reciever` of a serialized object have loaded classes for that object which are compatible with respect to serialization.
  + If the reciever has loaded a class for the object that has `different` UID than that of corresponding sender’s class, the de-serialization will result in an `InvalidClassException`.
  + A Serializable class can declare its own UID explicitly by declaring a `field name.` It must be `static, final and of type long.` It is also recommended to use `private modifier` for UID since it is not useful as `inherited member.`

+ Improve Java serialization performance
  + Mark the `unwanted or non Serializable attributes` as transient.
  + Save only the state of the object, not the derived attributes.
  + Serialize attributes only with `NON-default values.`
  
#### Serializable and Externalizable in Java

| Serializable                                                                                                                                                                                                                                                                                                              | Externalizable                                                                                                                   |   |   |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|---|---|
| Serializable has its own default serialization process, we just need to implement Serializable interface\. We can customize default serialization process by defining following methods in our class, readObject\(\) and writeObject\(\)\. Note: We are not overriding these methods, we are defining them in our class\. | Override writeExternal\(\) and readExternal\(\) for serialization process to happen when implementing Externalizable interface\. |   |   |
| This is a marker interface, does not have any methods\.                                                                                                                                                                                                                                                                   | This has 2 methods readExternal and writeExternal, hence it is not a marker interface\.                                          |   |   |
| Constructor is not called during de\-serialization\.                                                                                                                                                                                                                                                                      | Constructor is called during de\-serialization\.                                                                        

#### Compatible and In-compatible changes in Java serialization

+ If a newer version of a serialized object has to be compatible with an older version, it is important that the newer version abides by the rules for compatible and incompatible changes.

+ A compatible change is one that can be made to a new version of the class, which still keeps the stream compatible with older versions of the class. Examples of `compatible changes` are
  + `Addition of new fields` or classes does not affect serialization, as any new data in the stream is simply ignored by older versions. When the instance of an older version of the class is `deserialized`, the newly added field will be set to its `default value.`
  + You can field change `access modifiers` like private, public, protected or package as they are not reflected to the serial stream.
  + You can change a `transient or static field` to a non-transient or non-static field, as it is similar to `adding a field.`
  + You can change the access modifiers for constructors and methods of the class. For instance a previously private method can now be made public, an instance method can be changed to static, etc. The only exception is that you cannot change the default signatures for readObject() and writeObject() if you are implementing custom serialization. The serialization process looks at only instance data, and not the methods of a class.

+ Changes which would render the stream `incompatible` are
  + Once a class implements the Serializable interface, you `cannot` later make it implement the `Externalizable` interface, since this will result in the creation of an incompatible stream.
  + `Deleting fields` can cause a problem. Now, when the object is serialized, an earlier version of the class would set the old field to its default value since nothing was available within the stream. Consequently, this default data may lead the newly created object to assume an invalid state.
  + Changing a `non-static` into static or `non-transient` into transient is not permitted as it is equivalent to `deleting fields.`
  + You also cannot change the `field types` within a class, as this would cause a failure when attempting to read in the original field into the new field.
  + You cannot alter the `position of the class` in the class hierarchy. Since the fully-qualified class name is written as part of the bytestream, this change will result in the creation of an incompatible stream.
  + You cannot change the `name of the class` or the `package it belongs` to, as that information is written to the stream during serialization.

#### Data serialization in Redisson

+ Add config mode before create Redis client:

    ```java
        Config config = new Config();
        config.setCodec(new FstCodec());
    ```

+ Mode Json Jackson
  + Use @JsonProperty(value = "") to map between properties in Java class and field in Redis
  + Remove/Add a field in Java object: first, get old data in Redis and then re-write value with field changes

+ Mode FST (fast-serialization)
  + Change field name, remove field
  + Add new field: create new class (add new field), get and delete old bucket in Redis, store new bucket with new class