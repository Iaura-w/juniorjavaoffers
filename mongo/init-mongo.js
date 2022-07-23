db.createUser(
    {
        user: "lauur",
        pwd: "lauur",
        roles: [
            {
                role: "readWrite",
                db: "offers"
            }
        ]
    }
)