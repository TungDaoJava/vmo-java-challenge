print("Started Adding the Users.");

const adminDb = db.getSiblingDB("admin");

adminDb.createUser({
    user: "mongock",
    pwd: "password",
    roles: [
        { role: "dbOwner", db: "applications" },
        { role: "readWrite", db: "applications" }
    ]
});

adminDb.createUser({
    user: "app",
    pwd: "apppassword",
    roles: [
        { role: "readWrite", db: "applications" }
    ]
});


print("End Adding the User Roles.");
