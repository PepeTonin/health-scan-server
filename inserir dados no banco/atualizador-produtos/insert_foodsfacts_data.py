import pandas as pd
import mysql.connector

print("Atualizador: Processando arquivo de saida...")
csv_file_path = 'saida.csv'
selected_columns = ['image_url', 'categories', 'code', 'product_name', 'generic_name']

def process_chunk(chunk):
    return chunk[selected_columns]

def mapOnSave(row):
    for clm in selected_columns:
        if(pd.isna(row[clm])):
            row[clm] = ""

num_chunks = 8  
chunk_size = 100000  

chunks = pd.read_csv(csv_file_path, sep=',', encoding='utf-8', chunksize=chunk_size)

result_chunks = []

for chunk in chunks:
    result_chunk = process_chunk(chunk)
    result_chunks.append(result_chunk)

df = pd.concat(result_chunks)

print("Atualizador: Arquivo de saida processado!\n")
try:
    db_connection = mysql.connector.connect(host='localhost', user='root', password='Pessoas*951*', database='healthscan_schema')
    print("Atualizador: Iniciando processo de inserção")

    cursor = db_connection.cursor()

    for index, row in df.iterrows():
        mapOnSave(row)
        cursor.execute("INSERT INTO produto (NOME, DESCRICAO, CATEGORIA, CODBARRA, IMAGE) VALUES (%s, %s, %s, %s, %s)", (row['product_name'], row['generic_name'], row['categories'], row['code'], row['image_url']))

    db_connection.commit()

except mysql.connector.Error as error:
    print('Atualizador: Erro ao inserir dados:', error)

finally:
    if 'cursor' in locals() and cursor is not None:
        cursor.close()

    if 'db_connection' in locals() and db_connection is not None:
        db_connection.close()

    print("Atualizador: Processo de atualização finalizado!")
