-- Database: onboarding_db

-- DROP DATABASE IF EXISTS onboarding_db;

CREATE DATABASE onboarding_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Mexico.1252'
    LC_CTYPE = 'Spanish_Mexico.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Tabla: Usuarios 
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, 
    nombre_completo VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: Datos Onboarding 
CREATE TABLE datos_onboarding (
    id SERIAL PRIMARY KEY,
    usuario_id INT REFERENCES usuarios(id) ON DELETE CASCADE,
    
    -- Grupo 1: Información personal 
    telefono_encriptado TEXT,
    fecha_nacimiento_encriptada TEXT,
    
    -- Grupo 2: Dirección 
    calle_encriptada TEXT,
    ciudad_encriptada TEXT,
    codigo_postal_encriptado TEXT,
    
    -- Grupo 3: Documentos oficiales
    tipo_documento VARCHAR(50),
    numero_documento_encriptado TEXT,
    
    activo BOOLEAN DEFAULT TRUE 
);

-- Tabla: Trazabilidad 
CREATE TABLE logs_trazabilidad (
    id SERIAL PRIMARY KEY,
    usuario_id INT, 
    accion VARCHAR(100) NOT NULL, 
    ip_origen VARCHAR(45) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contador_registros (
    id SERIAL PRIMARY KEY,
    total_usuarios INT DEFAULT 0
);
INSERT INTO contador_registros (total_usuarios) VALUES (0);

--- TRIGGER REQUERIDO

CREATE OR REPLACE FUNCTION funcion_incrementar_contador()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE contador_registros SET total_usuarios = total_usuarios + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_nuevo_usuario
AFTER INSERT ON usuarios
FOR EACH ROW
EXECUTE FUNCTION funcion_incrementar_contador();

--- STORED PROCEDURE 
CREATE OR REPLACE PROCEDURE dar_de_baja_usuario(p_usuario_id INT)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE usuarios SET activo = FALSE WHERE id = p_usuario_id;
    UPDATE datos_onboarding SET activo = FALSE WHERE usuario_id = p_usuario_id;
END;
$$;