# Comandos do protocolo de comunicação.
Protocolo de comunicação de uma interface de comandos em texto com opção de interface gráfica que permite interação com um sistema que simula o problema do **Jantar dos Filósofos**. Aqui está uma explicação detalhada de cada comando do protocolo:

---

### **1. HELLO**
- **Descrição**: Inicia uma comunicação com o servidor.
- **Resposta**: Retorna uma saudação junto com o ID do cliente.
- **Exemplo**:  
  Entrada: `HELLO`  
  Saída: `HI [ID]`

---

### **2. SETID**
- **Descrição**: Define um ID específico para o cliente.
- **Funcionamento**: O comando espera um ID numérico anexado à mensagem. Se válido, atualiza o ID do cliente.
- **Respostas**:  
  - `NEW ID SAVED: [ID]` se o ID for válido.  
  - `INVALID ID.` se o ID for inválido.  
- **Exemplo**:  
  Entrada: `SETID 42`  
  Saída: `NEW ID SAVED: 42`

---

### **3. GETID**
- **Descrição**: Retorna o ID atual do cliente.
- **Exemplo**:  
  Entrada: `GETID`  
  Saída: `ID: [ID]`

---

### **4. SHOWTABLE**
- **Descrição**: Abre uma interface gráfica (GUI) para visualizar o estado do sistema, como filósofos e garfos.
- **Respostas**:  
  - Se a GUI não estiver aberta: Abre a GUI.  
  - Se já estiver aberta: `THE GUI IS ALREADY OPENED.`  
- **Exemplo**:  
  Entrada: `SHOWTABLE`  
  Saída: (abre a GUI ou mensagem de erro)

---

### **5. HIDETABLE**
- **Descrição**: Fecha a GUI aberta.
- **Respostas**:  
  - Se a GUI estiver aberta: Fecha a GUI.  
  - Se não estiver aberta: `NO GUI IS OPEN TO CLOSE.`  
- **Exemplo**:  
  Entrada: `HIDETABLE`  
  Saída: `NO GUI IS OPEN TO CLOSE.`

---

### **6. STATUS**
- **Descrição**: Retorna o status atual do servidor, incluindo o número de filósofos ativos e de garfos disponíveis.
- **Exemplo**:  
  Entrada: `STATUS`  
  Saída:  
  ```
  SERVER STATUS:
      NUMBER OF PHILOSOPHERS: [N]
      NUMBER OF FORKS: [N]
  ```

---

### **7. STATISTICS**
- **Descrição**: Mostra estatísticas de um filósofo associado ao cliente ou armazenado no log.
- **Funcionamento**:  
  - Se o cliente tem um filósofo ativo, retorna suas estatísticas.  
  - Caso contrário, verifica se há registros no log para o ID associado.  
- **Respostas**:  
  - Estatísticas do filósofo atual ou armazenado no log.  
  - `NO RECORD FOR PHILOSOPHER WITH ID: [ID]` se não houver dados.  
- **Exemplo**:  
  Entrada: `STATISTICS`  
  Saída:  
  ```
  SHOWING CURRENT: [NAME] STATISTICS:
      ID: [ID]
      NUMBER OF MEALS: [N]
      NUMBER OF THOUGHTS: [N]
  ```

---

### **8. START**
- **Descrição**: Cria um novo filósofo associado ao cliente.
- **Funcionamento**:  
  - Se o cliente já tem um filósofo ativo, retorna um erro.  
  - Se o ID atual for 0, cria um filósofo com um novo ID.  
  - Caso contrário, cria um filósofo com o ID especificado.  
- **Respostas**:  
  - Sucesso: `PHILOSOPHER: [NAME] CREATED WITH ID: [ID]`.  
  - Erro: `A PHILOSOHER WITH ID: [ID] ALREADY EXISTS!`.  
- **Exemplo**:  
  Entrada: `START`  
  Saída: `PHILOSOPHER: SOCRATES CREATED WITH ID: 1`

---

### **9. STOP**
- **Descrição**: Remove o filósofo associado ao cliente.
- **Respostas**:  
  - Sucesso: `PHILOSOPHER WITH ID: [ID] DELETED.`  
  - Erro: `ERROR: THIS CLIENT HAS NO ACCESS TO PHILOSOPHER WITH ID: [ID]`.  
- **Exemplo**:  
  Entrada: `STOP`  
  Saída: `PHILOSOPHER WITH ID: 1 DELETED.`

---

### **10. QUIT**
- **Descrição**: Encerra a conexão do cliente com o servidor.
- **Funcionamento**:  
  - Remove o filósofo associado, se existir.  
  - Fecha a conexão com o cliente.  
- **Exemplo**:  
  Entrada: `QUIT`  
  Saída: `CLOSING CONNECTION... [ID]`

---

### **11. Comandos não reconhecidos**
- **Descrição**: Para comandos inválidos, o servidor retorna uma mensagem de erro.
- **Resposta**: `ERROR: COMMAND UNRECOGNIZED!`  
- **Exemplo**:  
  Entrada: `FOO`  
  Saída: `ERROR: COMMAND UNRECOGNIZED!`

---

Esse protocolo permite gerenciar a simulação de forma interativa, seja criando filósofos, monitorando o estado do sistema ou ajustando a interface gráfica.