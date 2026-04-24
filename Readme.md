
## Tech Stack
* **Java 11** / **Maven**
* **JUnit 5** (Runner & Assertions)
* **Selenium WebDriver** & **RestAssured**
* **ExtentReports** (Reporting Engine)

##  Arquitetura
* **DriverFactory**: Gerenciamento de instâncias via `ThreadLocal` para suporte a paralelismo e isolamento de threads.
* **Page Objects**: Estrutura baseada em locators `By` dinâmicos, garantindo maior resiliência a mudanças no DOM e evitando erros de referência estática.
* **Extensions/Listeners**: Implementação de `BeforeAllCallback` e `AfterEachCallback` para gestão do ciclo de vida do relatório.

##  Execução
O projeto utiliza propriedades do sistema para definir o navegador e o ambiente:

```bash
# Execução padrão (Chrome)
mvn clean test

# Cross-browser e Headless
mvn test -Dbrowser=firefox
mvn test -Dbrowser=chrome-headless

# Executar apenas testes de API
mvn test -Dgroups="api"

# Executar apenas testes Web
mvn test -Dgroups="web"
```
##  Relatórios e Evidências

O framework utiliza o **ExtentReports** para gerar documentação visual das execuções. Os arquivos são salvos em `target/reports/` com nomenclatura baseada em timestamp (`Relatorio_dd-MM-yyyy_HHmm.html`).

### Recursos do Report:
* **Análise de Asserções**: Captura e exibe mensagens detalhadas de erro.
* **Dashboard de Metadados**: O cabeçalho do relatório registra automaticamente:
    * Usuário responsável pela execução;
    * Sistema Operacional;
    * Navegador utilizado;

## Estrutura do Projeto

A organização dos pacotes segue as melhores práticas de separação de responsabilidades:

```text
src/
 ├── main/java/com/agibank/
 │   ├── pages/            
 │   └── utils/            
 └── test/java/com/agibank/
     ├── web/              
     └── api/
```
## Teste de Perfomance
### 1. Teste de Carga 

* **Configuração do Cenário:**
    * **Threads (Usuários Simultâneos):** 40.
    * **Ramp-up:** 60 segundos (subida gradual para estabilização da infraestrutura).
    * **Duração:** 180 segundos.
* **Resultados:**
    * **Amostras Totais:** 10.462 requisições processadas.
    * **Taxa de Erro:** 0.0% (Estabilidade absoluta durante toda a execução).
    * **90th Percentile:** 1178.7 ms (Abaixo do limite de 2s estabelecido).
    * **Throughput Real:** 57.61 req/s.


### 2. Teste de Pico 

* **Configuração:** 100 threads | Ramp-up: 1s | Duração: 60s 
* **Resultados:**
    * **Amostras Totais:** 5.834 requisições.
    * **Taxa de Erro:** 6.87% (Identificado ponto de saturação).
    * **90% Percentile:** 2455.5 ms (Excedeu o SLA de 2s durante o pico).
    * **Throughput:** 88.10 req/s.
   
**Sobre o critério de aceitação:** O ambiente blazedemo.com apresenta um comportamento robusto para cargas estáveis de até 60 req/s. No entanto, as execuções isoladas provaram que a infraestrutura atual possui um gargalo físico que impede o atingimento seguro da meta de 250 RPS.

### 3. Execução via Linha de Comando (CLI Mode)

O comando padrão para execução é:
jmeter -n -t [caminho_do_script.jmx] -l [caminho_do_log.jtl] -e -o [diretorio_do_relatorio]

-n: Modo não-GUI (CLI).

-t: Caminho para o ficheiro .jmx.

-l: Ficheiro de log para gravar os resultados brutos.

-e -o: Gera o relatório em HTML automaticamente ao final do teste.

### Organização de Arquivos
* **Scripts JMeter:** [`src/performance/`](./src/performance/)
* **Relatórios de Execução:** [`src/performance/Relatorios/`](./src/performance/Relatorios/)
