package persistence;

import org.json.JSONObject;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represent a writable interface which a class has to implement to be written
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
