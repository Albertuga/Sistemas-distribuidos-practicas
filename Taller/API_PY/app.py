from flask import Flask, jsonify
import requests
import psycopg2
import os

app = Flask(__name__)

# Endpoint de comprobación
@app.route('/api/status', methods=['GET'])
def status():
    return jsonify({"status": "UP", "message": "API de Python funcionando correctamente"}), 200

# 1. Excepciones de apertura y lectura de archivos
@app.route('/api/test/file', methods=['GET'])
def test_file_exception():
    try:
        # Intentamos abrir un archivo que no existe
        with open('archivo_inexistente.txt', 'r') as file:
            content = file.read()
        return jsonify({"message": "Archivo leído", "content": content}), 200
    except FileNotFoundError as e:
        # Devolvemos un código 500 y el detalle del error
        return jsonify({"error": "FILE_ERROR", "details": str(e)}), 500

# 2. Excepciones de accesos a bases de datos
@app.route('/api/test/db', methods=['GET'])
def test_db_exception():
    try:
        # Intentamos conectar a una base de datos inexistente o con credenciales erróneas
        conn = psycopg2.connect(
            dbname="base_datos_inventada",
            user="usuario_falso",
            password="password",
            host="postgres-db", # Usamos el contenedor de tu docker-compose
            port="5432"
        )
        cur = conn.cursor()
        cur.execute("SELECT * FROM tabla_inexistente")
        conn.close()
        return jsonify({"message": "Consulta exitosa"}), 200
    except psycopg2.Error as e:
        return jsonify({"error": "DB_ERROR", "details": str(e)}), 500

# 3. Excepciones de llamadas a APIs de terceros (pokemon)
@app.route('/api/test/pokemon', methods=['GET'])
def test_pokemon_exception():
    try:
        # Buscamos un pokemon que no existe en la PokeAPI para forzar un error 404
        response = requests.get("https://pokeapi.co/api/v2/pokemon/agumon-no-es-un-pokemon", timeout=5)
        response.raise_for_status() # Esto lanza una excepción si el código HTTP es de error (ej: 404)
        return jsonify({"message": "Pokemon encontrado", "data": response.json()}), 200
    except requests.exceptions.RequestException as e:
        return jsonify({"error": "API_THIRD_PARTY_ERROR", "details": str(e)}), 500

if __name__ == '__main__':
    # Ejecutamos la app en el puerto 5000 accesible desde fuera del contenedor
    app.run(host='0.0.0.0', port=5000)