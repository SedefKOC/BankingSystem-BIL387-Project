# Database Scripts

Store SQL schema, seed data, and migration notes here. Keep sensitive data (passwords, dumps) out of version control.

## Login Table Quick Start

```
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(16) NOT NULL CHECK (role IN ('ADMIN', 'CUSTOMER'))
);

INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'ADMIN'),
    ('sedef', 'customer123', 'CUSTOMER')
ON CONFLICT (username) DO NOTHING;
```

> Not: Parolalar şu an sade metin olarak tutuluyor. Prod ortamda mutlaka hash + salt kullanılmalı.
