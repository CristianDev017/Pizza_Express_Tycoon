CREATE DATABASE IF NOT EXISTS pizza_express_tycoon_db;
USE pizza_express_tycoon_db;

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    id_sucursal INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    id_sucursal INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE nivel (
    id_nivel INT AUTO_INCREMENT PRIMARY KEY,
    numero_nivel INT NOT NULL,
    tiempo_base_segundos INT NOT NULL,
    pedidos_para_subir INT NOT NULL
);

CREATE TABLE partida (
    id_partida INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_sucursal INT NOT NULL,
    puntaje INT DEFAULT 0,
    nivel_alcanzado INT DEFAULT 1,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('EN_CURSO', 'TERMINADA') DEFAULT 'EN_CURSO',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_partida INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    tiempo_limite INT NOT NULL,
    estado ENUM('RECIBIDA', 'PREPARANDO', 'EN_HORNO', 'ENTREGADA', 'CANCELADA', 'NO_ENTREGADO') DEFAULT 'RECIBIDA',
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_producto INT,
    cantidad INT,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE historial_estado (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    estado ENUM('RECIBIDA', 'PREPARANDO', 'EN_HORNO', 'ENTREGADA', 'CANCELADA', 'NO_ENTREGADO') NOT NULL,
    descripcion VARCHAR(200),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

INSERT INTO rol (nombre) VALUES ('Super Admin'), ('Administrador'), ('Jugador');

INSERT INTO sucursal (nombre, direccion, estado) VALUES ('Sucursal Zona 1', 'Central', TRUE);

INSERT INTO usuario (nombre, username, password, id_rol, id_sucursal)
VALUES ('Super Admin', 'admin', '1234', 1, 1);

INSERT INTO nivel (numero_nivel, tiempo_base_segundos, pedidos_para_subir) VALUES
(1, 60, 5),
(2, 50, 5),
(3, 40, 0);