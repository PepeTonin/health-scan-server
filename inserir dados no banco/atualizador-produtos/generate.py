import pandas as pd

print("Atualizador: Iniciando processo de inserção de produtos")

csv_file_path = 'entrada.csv'
selected_columns = ['image_url', 'categories', 'code', 'product_name', 'generic_name']

print("Atualizador: Abrindo arquivo de entrada")
def process_chunk(chunk):
    chunk = chunk[chunk['countries'] == 'Brazil']

    return chunk[selected_columns]

num_chunks = 8  
chunk_size = 100000  


print("Atualizador: Lendo arquivo de entrada: "+ csv_file_path + " --- chunks: "+str(num_chunks)+" chunk size: "+str(chunk_size))
chunks = pd.read_csv(csv_file_path, sep='\t', encoding='utf-8', chunksize=chunk_size)
print("Atualizador: Arquivo de entrada lido")

result_chunks = []

print("Atualizador: Iniciando processamento dos chunks")
for chunk in chunks:
    result_chunk = process_chunk(chunk)
    result_chunks.append(result_chunk)

result_df = pd.concat(result_chunks)
print("Atualizador: fim do processamento dos chunks")
print("Atualizador: gravando arquivo de saida...")
output_csv_file_path = 'saida.csv'
result_df.to_csv(output_csv_file_path, sep=',', index=False)
print("Atualizador: arquivo de saida gravado")
