CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              restaurant_id BIGINT NOT NULL,
                              availability_slot_id BIGINT NOT NULL,
                              customer_name VARCHAR(120) NOT NULL,
                              customer_email VARCHAR(255) NOT NULL,
                              party_size INT NOT NULL CHECK (party_size > 0),
                              scheduled_at TIMESTAMPTZ NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_reservations_restaurant_id ON reservations(restaurant_id);
CREATE INDEX idx_reservations_restaurant_date ON reservations(restaurant_id, scheduled_at);
CREATE INDEX idx_reservations_status ON reservations(status);

--Tanto Eu quanto a Jéssica pensamos que seats reservedque não est´aqui e  está nos schemas devia ser boolean, se bem que caso o vslor de party size seja superior a a0 este valor seria sempr everdadeiro