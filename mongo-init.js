db.createUser(
        {
            user: "root",
            pwd: "example",
            roles: [
                {
                    role: "readWrite",
                    db: "rest_proxy_stub"
                }
            ]
        }
);