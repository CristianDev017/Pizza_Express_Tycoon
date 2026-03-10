-- Base de datos para el proyecto Pizza Express Tycoon

CREATE DATABASE IF NOT EXISTS pizza_express_tycoon_db;
USE pizza_express_tycoon_db;

-- aqui guardo los 3 roles que puede tener un usuario
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- las sucursales del restaurante, cada una tiene nombre, direccion y si esta activa o no
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    estado BOOLEAN DEFAULT TRUE -- true = activa, false = inactiva
);

-- tabla de usuarios, cada usuario tiene un rol asignado y pertenece a una sucursal
-- el username tiene que ser unico para que no se repitan
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

-- productos del menu, cada producto pertenece a una sucursal especifica
-- asi cada sucursal puede tener su propio menu
CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    id_sucursal INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

-- aqui guardo la configuracion de cada nivel
-- tiempo_base_segundos es cuanto tiempo tiene el jugador por pedido
-- pedidos_para_subir es cuantos pedidos tiene que completar para pasar al siguiente nivel
CREATE TABLE nivel (
    id_nivel INT AUTO_INCREMENT PRIMARY KEY,
    numero_nivel INT NOT NULL,
    tiempo_base_segundos INT NOT NULL,
    pedidos_para_subir INT NOT NULL
);

-- cada vez que un jugador juega se crea una partida
-- guarda el puntaje final y el nivel que alcanzo
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

-- cada pedido que llega durante una partida se guarda aqui
-- tiene su propio tiempo limite y va cambiando de estado conforme el jugador lo atiende
CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_partida INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    tiempo_limite INT NOT NULL,
    -- los estados van en orden: RECIBIDA -> PREPARANDO -> EN_HORNO -> ENTREGADA
    -- si el jugador cancela queda en CANCELADA, si se acaba el tiempo queda en NO_ENTREGADO
    estado ENUM('RECIBIDA', 'PREPARANDO', 'EN_HORNO', 'ENTREGADA', 'CANCELADA', 'NO_ENTREGADO') DEFAULT 'RECIBIDA',
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- aqui se guardan los productos que tiene cada pedido

CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_producto INT,
    cantidad INT,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- cada vez que un pedido cambia de estado se registra aqui
-- sirve para llevar el historial completo de lo que paso con cada pedido
CREATE TABLE historial_estado (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    estado ENUM('RECIBIDA', 'PREPARANDO', 'EN_HORNO', 'ENTREGADA', 'CANCELADA', 'NO_ENTREGADO') NOT NULL,
    descripcion VARCHAR(200),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

-- inserto los 3 roles que necesita el sistema
INSERT INTO rol (nombre) VALUES ('Super Admin'), ('Administrador'), ('Jugador');

-- sucursal base, la necesito para poder crear el usuario admin
INSERT INTO sucursal (nombre, direccion, estado) VALUES ('Sucursal Zona 1', 'Central', TRUE);

-- usuario administrador por defecto para poder entrar al sistema
-- credenciales: admin / 1234
INSERT INTO usuario (nombre, username, password, id_rol, id_sucursal)
VALUES ('Super Admin', 'admin', '1234', 1, 1);

-- los 3 niveles del juego con sus tiempos y pedidos para subir
INSERT INTO nivel (numero_nivel, tiempo_base_segundos, pedidos_para_subir) VALUES
(1, 60, 5),
(2, 50, 5),
(3, 40, 0);
