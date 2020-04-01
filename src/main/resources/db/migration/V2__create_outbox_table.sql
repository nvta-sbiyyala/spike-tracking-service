create table outbox
(
    uuid       UUID NOT NULL,
    event_type TEXT NOT NULL,
    payload    TEXT NOT NULL
)