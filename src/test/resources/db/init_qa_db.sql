\c qa

-- ======================
-- Permissions
-- ======================
GRANT ALL ON SCHEMA public TO qa_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO qa_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON SEQUENCES TO qa_user;

-- ======================
-- Scenario table FIRST
-- ======================
CREATE TABLE IF NOT EXISTS scenario_execution (
                                                  scenario_id UUID PRIMARY KEY,
                                                  run_id UUID NOT NULL,
                                                  feature_name TEXT NOT NULL,
                                                  scenario_name TEXT NOT NULL,
                                                  final_status VARCHAR(10),
                                                  start_time BIGINT NOT NULL,
                                                  end_time BIGINT
);

-- ======================
-- API table SECOND
-- ======================
CREATE TABLE IF NOT EXISTS api_execution (
                                             id SERIAL PRIMARY KEY,
                                             scenario_id UUID NOT NULL,
                                             api_name TEXT,
                                             method TEXT,
                                             endpoint TEXT,
                                             status VARCHAR(10),
                                             http_status INT,
                                             duration_ms BIGINT,
                                             created_at TIMESTAMP DEFAULT now(),
                                             CONSTRAINT fk_scenario
                                                 FOREIGN KEY (scenario_id)
                                                     REFERENCES scenario_execution(scenario_id)
                                                     ON DELETE CASCADE
);

-- ======================
-- Indexes
-- ======================
CREATE INDEX IF NOT EXISTS idx_scenario_run_feature
    ON scenario_execution(run_id, feature_name);

CREATE INDEX IF NOT EXISTS idx_api_scenario
    ON api_execution(scenario_id);

-- ======================
-- Final grants (important)
-- ======================
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO qa_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO qa_user;