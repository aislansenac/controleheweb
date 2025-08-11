$(document).ready(function () {
    const toggle = document.getElementById('switch');
    const inputEstilo = document.getElementById('inputEstilo');
    const formTema = document.getElementById('formTema');
    const icone = document.getElementById('temaIcone');

    toggle.addEventListener('change', () => {
        const novoEstilo = toggle.checked ? 'escuro' : 'claro';
        inputEstilo.value = novoEstilo;
        icone.textContent = novoEstilo === 'escuro' ? '☀️' : '🌙';
        formTema.submit();
    });

});

// Ativar aba "Minhas Horas Extras" ao carregar a página
window.addEventListener('DOMContentLoaded', () => {
    const abaMinhas = document.getElementById('tab-minhas');
    if (abaMinhas) {
        abaMinhas.click();
    }
});

const nome = document.getElementById('nomeFuncionario').textContent;
const perfil = document.getElementById('funcaoUsuario').textContent;


const menuAbas = document.getElementById('menuAbas');
const conteudoAbas = document.getElementById('conteudoAbas');

menuAbas.addEventListener('click', (e) => {
    if (e.target.classList.contains('nav-link')) {
        document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
        e.target.classList.add('active');

        const abaId = e.target.id.replace('tab-', '');

        if (abaId === 'funcionario') {
            conteudoAbas.innerHTML = `
                <div class="tab-pane fade show active">
                  <h5 class="mb-3">Cadastro de Funcionário</h5>
                  <form id="formCadastroUsuario" class="mb-4">
                    <div class="row g-3 mb-3">
                      <div class="col-md-6">
                        <label for="nome" class="form-label">Nome</label>
                        <input type="text" class="form-control" id="nome" required>
                      </div>
                      <div class="col-md-6">
                        <label for="cpf" class="form-label">CPF</label>
                        <input type="text" class="form-control" id="cpf" required>
                      </div>
                      <div class="col-md-6">
                        <label for="dataNascimento" class="form-label">Data de Nascimento</label>
                        <input type="date" class="form-control" id="dataNascimento" required>
                      </div>
                      <div class="col-md-6">
                        <label for="salario" class="form-label">Salário</label>
                        <input type="number" class="form-control" id="salario" required step="0.01" min="0" />
                      </div>
                      <div class="col-md-6">
                        <label for="cargo" class="form-label">Função ou Encargo</label>
                        <select id="cargo" class="form-select" required>
                        </select>
                      </div>

                    </div>
                    <div class="text-end">
                      <button type="submit" class="btn btn-success">Salvar</button>
                    </div>
                  </form>

                  <div class="mb-3 d-flex justify-content-between">
                        <input type="text" id="pesquisarNome" class="form-control w-50" placeholder="Pesquisar por nome">
                  </div>

                  <table class="table table-striped">
                    <thead>
                      <tr>
                        <th>Nome</th>
                        <th>CPF</th>
                        <th>Função</th>
                        <th>Salário</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody id="tabelaUsuarios">
                    </tbody>
                  </table>
                </div>`;

            setTimeout(() => {
                carregarCargo();
                carregaUsuarios();
                // Editar funcionário
                $(document).on('click', '.btn-editar', function () {
                    const id = $(this).data('id');

                    // Buscar os dados completos
                    $.get(`http://localhost:8080/funcionario/${id}`, function (func) {
                        const modal = new bootstrap.Modal(document.getElementById('modalEditarFuncionario'));
                        modal.show();
                        carregarCargoEdit(function () {
                            $('#editId').val(func.id);
                            $('#editNome').val(func.nome);
                            $('#editCpf').val(func.cpf);
                            $('#editNascimento').val(func.dataNascimento);
                            $('#editSalario').val(func.salario);
                            $('#editCargo').val(func.cargo.id || '');
                            $('#editSenha').val('');
                        });
                        
                    });
                });
                // Pesquisar
                document.getElementById('pesquisarNome').addEventListener('input', function () {
                    const filtro = this.value.toLowerCase();
                    const linhas = document.querySelectorAll('#tabelaUsuarios tr');

                    linhas.forEach(linha => {
                        const nome = linha.querySelector('td')?.textContent.toLowerCase();
                        if (nome && nome.includes(filtro)) {
                            linha.style.display = '';
                        } else {
                            linha.style.display = 'none';
                        }
                    });
                });
                // Submeter atualização
                $('#formEditarFuncionario').submit(function (e) {
                    e.preventDefault();

                    const id = $('#editId').val();

                    const payload = {
                        nome: $('#editNome').val(),
                        cpf: $('#editCpf').val(),
                        dataNascimento: $('#editNascimento').val(),
                        salario: parseFloat($('#editSalario').val()),
                        senha: $('#editSenha').val(),
                        ativo: true, // ou mantenha o valor atual
                        cargo: {
                            id: parseInt($('#editCargo').val())
                        }
                    };

                    $.ajax({
                        url: `http://localhost:8080/funcionario/atualizar/${id}`,
                        method: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(payload),
                        success: function () {
                            carregaUsuarios();
                            alert('Funcionário atualizado com sucesso!');
                            $('#modalEditarFuncionario').modal('hide');

                        },
                        error: function () {
                            alert('Erro ao atualizar funcionário.');
                        }
                    });
                });


                const cpfInput = document.getElementById('cpf');
                cpfInput.addEventListener('input', () => {
                    let value = cpfInput.value.replace(/\D/g, '');
                    if (value.length > 11)
                        value = value.slice(0, 11);

                    value = value
                            .replace(/(\d{3})(\d)/, '$1.$2')
                            .replace(/(\d{3})(\d)/, '$1.$2')
                            .replace(/(\d{3})(\d{1,2})$/, '$1-$2');

                    cpfInput.value = value;
                });
                // Corrige foco após fechar modal para evitar warning do aria-hidden
        $('#modalEditarFuncionario').on('hide.bs.modal', function () {
            const focused = document.activeElement;
            if ($(this).find(focused).length) {
                focused.blur();
            }
        });

        $('#modalEditarFuncionario').on('hidden.bs.modal', function () {
            $('body').focus();
        });
            }, 50); // pequeno delay para garantir que o DOM foi atualizado

        } else if (abaId === 'senha') {
            conteudoAbas.innerHTML = `
                <div class="tab-pane fade show active">
                  <h5 class="mb-3">Alterar Senha</h5>
                  <form id="formAlterarSenha" class="w-50 w-md-50">
                    <div class="mb-3">
                      <label for="novaSenha" class="form-label">Nova Senha</label>
                      <input type="password" class="form-control" id="novaSenha" required>
                    </div>
                    <div class="mb-3">
                      <label for="confirmarNovaSenha" class="form-label">Confirmar Nova Senha</label>
                      <input type="password" class="form-control" id="confirmarNovaSenha" required>
                    </div>
                    <div class="text-end">
                      <button type="submit" class="btn btn-primary">Atualizar Senha</button>
                    </div>
                  </form>
                </div>`;
            setTimeout(() => {
                document.getElementById('formAlterarSenha').addEventListener('submit', function (e) {
                    e.preventDefault();

                    const novaSenha = document.getElementById('novaSenha').value;
                    const confirmarNovaSenha = document.getElementById('confirmarNovaSenha').value;

                    if (novaSenha !== confirmarNovaSenha) {
                        alert('As senhas não coincidem.');
                        return;
                    }

                    const idUsuario = document.getElementById('idUsuario').value;

                    fetch(`http://localhost:8080/funcionario/alterar-senha/${idUsuario}`, {
                        method: 'PATCH',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({novaSenha: novaSenha})
                    })
                            .then(res => {
                                if (res.ok) {
                                    alert('Senha atualizada com sucesso!');
                                    document.getElementById('formAlterarSenha').reset();
                                } else {
                                    alert('Erro ao atualizar senha.');
                                }
                            })
                            .catch(() => alert('Erro ao conectar ao servidor.'));
                });
            }, 50);
        } else if (abaId === 'cadastro') {
            conteudoAbas.innerHTML = `
    <div class="tab-pane fade show active">
      <h5 class="mb-3">Cadastro de Hora Extra</h5>
      <form id="formHoraExtra" class="mb-4">
        <div class="row g-3">
          <div class="col-md-4">
            <label for="dataHe" class="form-label">Data</label>
            <input type="date" class="form-control" id="dataHe" required>
          </div>
          <div class="col-md-4">
            <label for="horaInicio" class="form-label">Hora de Início</label>
            <input type="time" class="form-control" id="horaInicio" required>
          </div>
          <div class="col-md-4">
            <label for="horaTermino" class="form-label">Hora de Término</label>
            <input type="time" class="form-control" id="horaTermino" required>
          </div>
          <div class="col-md-4">
            <label for="vagas" class="form-label">Quantidade de Vagas</label>
            <input type="number" step="0.01" class="form-control" id="vagas" required>
          </div>
          <div class="col-12">
            <label for="observacao" class="form-label">Observação</label>
            <textarea id="observacao" class="form-control" rows="3"></textarea>
          </div>
        </div>
        <div class="text-end mt-3">
          <button type="submit" class="btn btn-success">Cadastrar Hora Extra</button>
        </div>
      </form>

      <div class="mb-3 d-flex gap-2 align-items-center">
        <input type="date" id="pesquisarData" class="form-control w-auto" placeholder="Pesquisar por data">
        <button id="btnPesquisar" class="btn btn-primary">Pesquisar</button>
      </div>

      <table class="table table-striped">
        <thead>
          <tr>
            <th>Data</th>
            <th>Início</th>
            <th>Término</th>
            <th>Qtd</th>
            <th>Observação</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody id="tabelaHorasExtras">
        </tbody>
      </table>
    </div>
  `;
            setTimeout(() => {
                carregaHorasExtras();
                document.getElementById('formHoraExtra').addEventListener('submit', function (e) {
                    e.preventDefault();
                    const idUsuario = document.getElementById('idUsuario').value;
                    const novaHe = {
                        data: $('#dataHe').val(),
                        horaInicio: $('#horaInicio').val(),
                        horaTermino: $('#horaTermino').val(),
                        vagas: $('#vagas').val(),
                        observacao: $('#observacao').val(),
                        criador: {
                            id: idUsuario
                        }
                    };

                    $.ajax({
                        url: 'http://localhost:8080/horaextra/adicionar',
                        method: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(novaHe),
                        success: function () {
                            alert('Hora Extra cadastrada com sucesso!');
                            $('#formHoraExtra')[0].reset(); // limpa o formulário
                            carregaHorasExtras();
                        },
                        error: function (xhr) {
                            console.error(xhr.responseText);
                            alert('Erro ao cadastrar o HE.');
                        }
                    });
                });
            }, 50);
        } else if (abaId === 'minhas') {
            conteudoAbas.innerHTML = `
    <div class="tab-pane fade show active">
      <h5 class="mb-3">Minhas Horas Extras</h5>

      <div class="row g-3 align-items-end mb-4">
        <div class="col-md-4">
          <label for="mes" class="form-label">Mês</label>
          <select id="mes" class="form-select">
            <option value="" disabled selected>Selecione o mês</option>
            <option value="0">Todos</option>
            <option value="1">Janeiro</option>
            <option value="2">Fevereiro</option>
            <option value="3">Março</option>
            <option value="4">Abril</option>
            <option value="5">Maio</option>
            <option value="6">Junho</option>
            <option value="7">Julho</option>
            <option value="8">Agosto</option>
            <option value="9">Setembro</option>
            <option value="10">Outubro</option>
            <option value="11">Novembro</option>
            <option value="12">Dezembro</option>
          </select>
        </div>
        <div class="col-md-3">
          <label for="ano" class="form-label">Ano</label>
          <input type="number" id="ano" class="form-control" value="${new Date().getFullYear()}">
        </div>
        <div class="col-md-3">
          <button class="btn btn-primary mt-3" id="btnFiltrarHoras">Filtrar</button>
        </div>
        <div class="mt-3 text-end">
        <strong>Total de Horas Realizadas:</strong>
        <span id="totalHoras" class="ms-2"></span> horas
      </div>
      </div>

      <table class="table table-striped">
  <thead>
    <tr>
      <th>Data</th>
      <th>Início</th>
      <th>Término</th>
      <th>Criador</th>
      <th>Aprovada</th>
      <th class="text-center">Ver mais</th>
    </tr>
  </thead>
  <tbody id="tabelaMinhasHoras">
    
  </tbody>
</table>
    </div>
  `;
            setTimeout(() => {
                carregaMinhaHorasExtras();
            }, 50);

        } else if (abaId === 'inscrever') {
            conteudoAbas.innerHTML = `
    <div class="tab-pane fade show active">
  <h5 class="mb-3">Hora Extra Disponíveis</h5>
  <!-- Campo de pesquisa por data -->
  <div class="mb-3 d-flex gap-2 flex-wrap">
    <input type="date" id="filtroDataInscricao" class="form-control w-auto" />
    <button class="btn btn-primary" id="btnPesquisarInscricao">Pesquisar</button>
  </div>
  <table class="table table-striped" id="tabelaInscrever">
    <thead>
      <tr>
        <th>Data</th>
        <th>Início</th>
        <th>Término</th>
        <th>Criador</th>
        <th>Observação</th>
        <th class="text-center">Participar</th>
      </tr>
    </thead>
    <tbody id="tabelaInscricaoHoras">
    </tbody>
  </table>
</div>`;
            setTimeout(() => {
                carregaHorasExtrasInscrever();
            }, 50);
        } else if (abaId === 'relatorio') {
            conteudoAbas.innerHTML = `
<div class="row g-2 mb-4 align-items-end">
  <div class="col-auto">
    <label for="filtroMesRelatorio" class="form-label mb-1">Mês</label>
    <select id="filtroMesRelatorio" class="form-select form-select-sm">
      <option value="">Todos</option>
      <option value="01">Janeiro</option>
      <option value="02">Fevereiro</option>
      <option value="03">Março</option>
      <option value="04">Abril</option>
      <option value="05">Maio</option>
      <option value="06">Junho</option>
      <option value="07">Julho</option>
      <option value="08">Agosto</option>
      <option value="09">Setembro</option>
      <option value="10">Outubro</option>
      <option value="11">Novembro</option>
      <option value="12">Dezembro</option>
    </select>
  </div>
  <div class="col-auto">
    <label for="filtroAnoRelatorio" class="form-label mb-1">Ano</label>
    <input type="number" id="filtroAnoRelatorio" class="form-control form-control-sm" placeholder="2025" />
  </div>
  <div class="col-auto mt-auto">
    <button class="btn btn-sm btn-primary" id="btnFiltrarRelatorio">Filtrar</button>
  </div>

   <!-- Tabela de resultados -->
  <table class="table table-hover align-middle">
    <thead class="table-dark">
      <tr>
        <th>CPF</th>
        <th>Nome</th>
         <th>Data de Nascimento</th>
        <th>Cargo</th>
        <th>Salário</th>
        <th>Total de Horas Extras</th>
        <th class="text-center">Ver Mais</th>
      </tr>
    </thead>
    <tbody id="tabelaRelatorio">
    </tbody>
  </table>
</div>


  `;
            setTimeout(() => {
                carregaRelatorio();
            }, 50);
        } else if (abaId === 'aprovar') {
            conteudoAbas.innerHTML = `
    <div class="tab-pane fade show active">
  <h5 class="mb-3">Aprovar Hora Extra</h5>

  <table class="table table-hover align-middle">
    <thead class="table-dark">
      <tr>
        <th>Data</th>
        <th>Início</th>
        <th>Término</th>
        <th>Criador</th>
        <th>Realizada por</th>
        <th class="text-center">Ações</th>
      </tr>
    </thead>
    <tbody id="tabelaAprovacaoHoras">
      
    </tbody>
  </table>
</div>`;
            setTimeout(() => {
                carregaAprovaHoraExtras();
            }, 50);
        }
    }
    function carregarCargo() {
        $.ajax({
            url: 'http://localhost:8080/cargo/listar',
            method: 'GET',
            success: function (data) {
                const selectCargo = $('#cargo');
                selectCargo.empty();
                selectCargo.append('<option value="">Selecione o Cargo</option>');

                // Ordenar data pelo nome
                data.sort(function (a, b) {
                    return a.tipo.toLowerCase().localeCompare(b.tipo.toLowerCase());
                });

                data.forEach(function (cargo) {
                    selectCargo.append(`<option value="${cargo.id}">${cargo.tipo}</option>`);
                });
            },
            error: function () {
                alert('Erro ao carregar os dados API Cargo.');
            }
        });
    }

    function carregarCargoEdit(callback) {
        $.ajax({
            url: 'http://localhost:8080/cargo/listar',
            method: 'GET',
            success: function (data) {
                const selectCargo = $('#editCargo');
                selectCargo.empty();
                selectCargo.append('<option value="">Selecione o cargo</option>');

                // Ordenar pelo nome
                data.sort((a, b) => a.tipo.toLowerCase().localeCompare(b.tipo.toLowerCase()));

                data.forEach(cargo => {
                    selectCargo.append(`<option value="${cargo.id}">${cargo.tipo}</option>`);
                });

                // Executa o callback se foi passado
                if (callback)
                    callback();
            },
            error: function () {
                alert('Erro ao carregar os dados API Cargo.');
            }
        });
    }

    function carregaRelatorio() {
        $.ajax({
            url: 'http://localhost:8080/horaextra/funcionarios-por-horas',
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaRelatorio');
                tabela.empty();
                if (!data || data.length === 0) {
                    tabela.append(`
                        <tr>
                            <td colspan="7" class="text-center text-muted">
                                Nenhuma hora extra efetuada
                            </td>
                        </tr>
                    `);
                    return; // para evitar continuar o código
                }
                data.forEach(func => {
                    const salarioFormatado = func.salario.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'});

                    const linha = `<tr>
                    <td>${func.cpf}</td>
                    <td>${func.nome}</td>
                    <td>${formatarData(func.data)}</td>
                    <td>${func.cargo}</td>
                    <td>${salarioFormatado}</td>
                    <td>${func.totalHoras}</td>
                    <td class="text-center">
                      <button class="btn btn-outline-primary btn-sm btn-vermais" title="Ver Mais" 
                            data-id="${func.id}" 
                            data-cpf="${func.cpf}" 
                            data-nome="${func.nome}" 
                            data-cargo="${func.cargo}" 
                            data-salario="${func.salario.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'})}">
                        <i class="bi bi-eye"></i>
                      </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('.btn-vermais').click(function (e) {
                    e.preventDefault();
                    const funcionarioId = $(this).data('id');
                    let botao = $(this);

                    $.ajax({
                        url: `http://localhost:8080/horaextra/listar-inscritas/${funcionarioId}`,
                        method: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            // Exibe o modal
                            // Abre o modal Bootstrap
                            const tabela = $('#tabelaHorasExtrasModal');
                            tabela.empty();
                            const cpf = botao.data("cpf");
                            const nome = botao.data("nome");
                            const cargo = botao.data("cargo");
                            const salario = botao.data("salario");

                            $('#funcNomeM').text(nome);
                            $('#funcCpfM').text(cpf);
                            $('#funcCargoM').text(cargo);
                            $('#funcSalarioM').text(salario);

                            data.forEach(he => {
                                const inscricao = he.funcionariosInscritos.find(fi => fi.funcionario.id === funcionarioId);
                                const verificada = inscricao ? inscricao.verificada : 'não'; // evita erro
                                const aprovada = inscricao ? inscricao.aprovada : 'não';

                                let statusTexto;
                                let statusClasse;

                                if (!verificada && !aprovada) {
                                    statusTexto = 'Pendente';
                                    statusClasse = 'text-warning fw-bold';
                                } else if (verificada && aprovada) {
                                    statusTexto = 'Aprovada';
                                    statusClasse = 'text-success fw-bold';
                                } else if (verificada && !aprovada) {
                                    statusTexto = 'Reprovada';
                                    statusClasse = 'text-danger fw-bold';
                                } else {
                                    statusTexto = 'Indefinido';
                                    statusClasse = 'text-secondary fw-bold';
                                }
                                const linha = `<tr>

                                <td>${formatarData(he.data)}</td>
                                <td>${he.horaInicio}</td>
                                <td>${he.horaTermino}</td>
                                <td>${he.criador.nome}</td>
                                <td class="${statusClasse}">${statusTexto}</td>
                            </tr>`;
                                tabela.append(linha);
                            });
                            const modal = new bootstrap.Modal(document.getElementById('modalFuncionarioHoras'));
                            modal.show();


                        },
                        error: function () {
                            alert('Erro ao buscar lista.');
                        }
                    });

                });

            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
        // Corrige foco após fechar modal para evitar warning do aria-hidden
        $('#modalFuncionarioHoras').on('hide.bs.modal', function () {
            const focused = document.activeElement;
            if ($(this).find(focused).length) {
                focused.blur();
            }
        });

        $('#modalFuncionarioHoras').on('hidden.bs.modal', function () {
            $('body').focus();
        });
    }

    $('#formCadastroUsuario').submit(function (e) {
        e.preventDefault();
        const novoFuncionario = {
            nome: $('#nome').val(),
            cpf: $('#cpf').val(),
            dataNascimento: $('#dataNascimento').val(),
            salario: $('#salario').val(),
            senha: '748596',
            cargo: {
                id: $('#cargo').val()
            }
        };

        $.ajax({
            url: 'http://localhost:8080/funcionario/adicionar',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(novoFuncionario),
            success: function () {
                alert('Funcionario cadastrado com sucesso!');
                $('#formCadastroUsuario')[0].reset(); // limpa o formulário
                carregaUsuarios();
            },
            error: function (xhr) {
                console.error(xhr.responseText);
                alert('Erro ao cadastrar o Usuário.');
            }
        });
    });

    function carregaUsuarios() {
        const funcionarioId = parseInt($('#idUsuario').val(), 10);
        $.ajax({
            url: `http://localhost:8080/funcionario/listar/${funcionarioId}`,
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaUsuarios');
                tabela.empty();

                data.forEach(func => {
                    const salarioFormatado = func.salario.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'});
                    const linha = `<tr>
                    <td>${func.nome}</td>
                    <td>${func.cpf}</td>
                    <td>${func.cargo?.tipo || '-'}</td>
                    <td>${salarioFormatado}</td>
                    <td>
                        <button class="btn btn-sm btn-warning btn-editar" data-id="${func.id}">Editar</button>
                        <button class="btn btn-sm ${func.ativo ? 'btn-danger' : 'btn-success'} btn-toggle-ativo"
                                data-id="${func.id}" data-ativo="${func.ativo}">
                            ${func.ativo ? 'Inativar' : 'Ativar'}
                        </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('.btn-toggle-ativo').off('click').on('click', function () {
                    const id = $(this).data('id');
                    const ativoAtual = $(this).attr('data-ativo') === 'true';

                    $.ajax({
                        url: `http://localhost:8080/funcionario/atualizar-status/${id}`,
                        method: 'PATCH',
                        contentType: 'application/json',
                        data: JSON.stringify({ativo: !ativoAtual}),
                        success: function () {
                            alert('Status atualizado com sucesso!');
                            carregaUsuarios();
                        },
                        error: function () {
                            alert('Erro ao atualizar status do funcionário.');
                        }
                    });
                });
            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
    }

    function carregaHorasExtras() {
        $.ajax({
            url: 'http://localhost:8080/horaextra/listar',
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaHorasExtras');
                tabela.empty();

                data.forEach(he => {
                    const linha = `<tr>
                    
                    <td>${formatarData(he.data)}</td>
                    <td>${he.horaInicio}</td>
                    <td>${he.horaTermino}</td>
                    <td>${he.vagas}</td>
                    <td>${he.observacao}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-info" title="Ver mais">
                            <i class="bi bi-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Excluir">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('.btn-toggle-ativo').off('click').on('click', function () {
                    const id = $(this).data('id');
                    const ativoAtual = $(this).attr('data-ativo') === 'true';

                    $.ajax({
                        url: `http://localhost:8080/funcionario/atualizar-status/${id}`,
                        method: 'PATCH',
                        contentType: 'application/json',
                        data: JSON.stringify({ativo: !ativoAtual}),
                        success: function () {
                            alert('Status atualizado com sucesso!');
                            carregaUsuarios();
                        },
                        error: function () {
                            alert('Erro ao atualizar status do funcionário.');
                        }
                    });
                });
            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
    }

    function carregaHorasExtrasInscrever() {
        const funcionarioId = parseInt($('#idUsuario').val(), 10);
        $.ajax({
            url: `http://localhost:8080/horaextra/listar-disponiveis/${funcionarioId}`,
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaInscricaoHoras');
                tabela.empty();
                if (!data || data.length === 0) {
                    tabela.append(`
                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Nenhuma hora extra disponível
                            </td>
                        </tr>
                    `);
                    return; // para evitar continuar o código
                }
                data.forEach(he => {
                    const linha = `<tr>
                    
                    <td>${formatarData(he.data)}</td>
                    <td>${he.horaInicio}</td>
                    <td>${he.horaTermino}</td>
                    <td>${he.criador.nome}</td>
                    <td>${he.observacao}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-success btn-participar" data-id="${he.id}" title="Participar">
                            <i class="bi bi-person-arms-up"></i>
                        </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('.btn-participar').click(function (e) {
                    const confirmado = confirm("Atenção: Caso você se inscreva para a hora extra e não compareça, isso poderá resultar em advertências ou outras medidas administrativas.\n\
                            Deseja realmente se inscrever?");
                    if (confirmado) {
                        e.preventDefault();
                        const horaExtraId = $(this).data('id');


                        const funcHe = {
                            funcionario: {id: parseInt(funcionarioId)},
                            horaExtra: {id: horaExtraId}
                        };

                        $.ajax({
                            url: `http://localhost:8080/horaextra/inscrever`,
                            method: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify(funcHe),
                            success: function () {
                                alert('Inscrição realizada com sucesso!');
                                carregaHorasExtrasInscrever();
                            },
                            error: function () {
                                alert('Erro ao se inscrever.');
                            }
                        });
                    }
                });
            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
    }


    function carregaMinhaHorasExtras() {
        const funcionarioId = parseInt($('#idUsuario').val(), 10);

        $.ajax({
            url: `http://localhost:8080/horaextra/listar-inscritas/${funcionarioId}`,
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaMinhasHoras');
                let totalHoras = 0;

                tabela.empty();
                if (!data || data.length === 0) {
                    tabela.append(`
                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Nenhuma hora extra efetuada
                            </td>
                        </tr>
                    `);
                    $('#totalHoras').text('00:00'); // zera total também
                    return; // para evitar continuar o código
                }
                data.forEach(he => {
                    const inscricao = he.funcionariosInscritos.find(fi => fi.funcionario.id === funcionarioId);
                    const verificada = inscricao ? inscricao.verificada : 'não'; // evita erro
                    const aprovada = inscricao ? inscricao.aprovada : 'não';

                    if (verificada && aprovada) {
                        const [hIni, mIni] = he.horaInicio.split(':').map(Number);
                        const [hFim, mFim] = he.horaTermino.split(':').map(Number);

                        const duracao = (hFim + mFim / 60) - (hIni + mIni / 60);
                        if (duracao > 0)
                            totalHoras += duracao;
                    }

                    let statusTexto;
                    let statusClasse;

                    if (!verificada && !aprovada) {
                        statusTexto = 'Pendente';
                        statusClasse = 'text-warning fw-bold';
                    } else if (verificada && aprovada) {
                        statusTexto = 'Aprovada';
                        statusClasse = 'text-success fw-bold';
                    } else if (verificada && !aprovada) {
                        statusTexto = 'Reprovada';
                        statusClasse = 'text-danger fw-bold';
                    } else {
                        statusTexto = 'Indefinido';
                        statusClasse = 'text-secondary fw-bold';
                    }
                    const linha = `<tr>
                    
                    <td>${formatarData(he.data)}</td>
                    <td>${he.horaInicio}</td>
                    <td>${he.horaTermino}</td>
                    <td>${he.criador.nome}</td>
                    <td class="${statusClasse}">${statusTexto}</td>
                    <td class="text-center">
                        <button class="btn btn-outline-primary btn-sm btn-vermaisx" title="Ver Mais"
                            data-horainicio="${he.horaInicio}" 
                            data-datanasc="${he.data}" 
                            data-horatermino="${he.horaTermino}" 
                            data-vagas="${he.vagas}" 
                            data-observacao="${he.observacao}" >
                          <i class="bi bi-eye"></i>
                        </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('#totalHoras').text(decimalParaHoraMinutos(totalHoras));
                // Reatribui eventos após popular
                $('.btn-vermaisx').click(function (e) {

                    e.preventDefault();

                    let botao = $(this);

                    $.ajax({
                        url: `http://localhost:8080/horaextra/listar`,
                        method: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            // Exibe o modal
                            // Abre o modal Bootstrap
                            const dataNasc = botao.data("datanasc");
                            const horainicio = botao.data("horainicio");
                            const horatermino = botao.data("horatermino");
                            const vagas = botao.data("vagas");
                            const observacao = botao.data("observacao");

                            $('#heData').text(formatarData(dataNasc));
                            $('#heHoraInicio').text(horainicio);
                            $('#heHoraTermino').text(horatermino);
                            $('#heVagas').text(vagas);
                            $('#heObservacao').text(observacao);

                            const modal = new bootstrap.Modal(document.getElementById('modalHoraExtra'));
                            modal.show();


                        },
                        error: function () {
                            alert('Erro ao buscar lista.');
                        }
                    });
                });
            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
        // Corrige foco após fechar modal para evitar warning do aria-hidden
        $('#modalFuncionarioHoras').on('hide.bs.modal', function () {
            const focused = document.activeElement;
            if ($(this).find(focused).length) {
                focused.blur();
            }
        });

        $('#modalFuncionarioHoras').on('hidden.bs.modal', function () {
            $('body').focus();
        });
    }
    function carregaHorasExtras() {
        $.ajax({
            url: 'http://localhost:8080/horaextra/listar',
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaHorasExtras');
                tabela.empty();
                if (!data || data.length === 0) {
                    tabela.append(`
                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Nenhuma hora extra cadastrada
                            </td>
                        </tr>
                    `);
                    return; // para evitar continuar o código
                }
                data.forEach(he => {
                    const linha = `<tr>
                    
                    <td>${formatarData(he.data)}</td>
                    <td>${he.horaInicio}</td>
                    <td>${he.horaTermino}</td>
                    <td>${he.vagas}</td>
                    <td>${he.observacao}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-info" title="Ver mais">
                            <i class="bi bi-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Excluir">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>`;
                    tabela.append(linha);
                });

                $('.btn-toggle-ativo').off('click').on('click', function () {
                    const id = $(this).data('id');
                    const ativoAtual = $(this).attr('data-ativo') === 'true';

                    $.ajax({
                        url: `http://localhost:8080/funcionario/atualizar-status/${id}`,
                        method: 'PATCH',
                        contentType: 'application/json',
                        data: JSON.stringify({ativo: !ativoAtual}),
                        success: function () {
                            alert('Status atualizado com sucesso!');
                            carregaUsuarios(); // recarrega lista
                        },
                        error: function () {
                            alert('Erro ao atualizar status do funcionário.');
                        }
                    });
                });
            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });
    }
    function carregaAprovaHoraExtras() {
        const funcionarioId = parseInt($('#idUsuario').val(), 10);


        $.ajax({
            url: `http://localhost:8080/horaextra/listar-nao-verificadas`,
            method: 'GET',
            success: function (data) {
                const tabela = $('#tabelaAprovacaoHoras');
                tabela.empty();
                if (!data || data.length === 0) {
                    tabela.append(`
                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Nenhuma hora extra para aprovar
                            </td>
                        </tr>
                    `);
                    return; // para evitar continuar o código
                }

                data.forEach(he => {
                    he.funcionariosInscritos.forEach(inscrito => {
                        if (!inscrito.verificada) { // só adiciona se for false
                            const linha = `
                      <tr>
                        <td>${formatarData(he.data)}</td>
                        <td>${he.horaInicio}</td>
                        <td>${he.horaTermino}</td>
                        <td>${he.criador.nome}</td>
                        <td>${inscrito.funcionario.nome}</td>
                        <td class="text-center">
                            
                          <button class="btn btn-outline-primary btn-sm btn-ver-maisx" 
                            data-horainicio="${he.horaInicio}" 
                            data-datanasc="${he.data}" 
                            data-horatermino="${he.horaTermino}" 
                            data-vagas="${he.vagas}" 
                            data-observacao="${he.observacao}" 
                            title="Ver Mais">
                            <i class="bi bi-eye"></i>
                          </button>

                          <button class="btn btn-sm btn-success btn-aprovar" data-inscrito-id="${inscrito.id}" title="Aprovar">
                            <i class="bi bi-check-circle-fill"></i>
                          </button>

                          <button class="btn btn-sm btn-danger btn-reprovar" data-inscrito-id="${inscrito.id}" title="Reprovar">
                            <i class="bi bi-x-circle-fill"></i>
                          </button>
                        </td>
                      </tr>
                    `;
                            tabela.append(linha);
                        }
                    });
                });

                $('.btn-ver-maisx').click(function (e) {

                    e.preventDefault();

                    let botao = $(this);

                    $.ajax({
                        url: `http://localhost:8080/horaextra/listar`,
                        method: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            // Exibe o modal
                            // Abre o modal Bootstrap
                            const dataNasc = botao.data("datanasc");
                            const horainicio = botao.data("horainicio");
                            const horatermino = botao.data("horatermino");
                            const vagas = botao.data("vagas");
                            const observacao = botao.data("observacao");

                            $('#heData').text(formatarData(dataNasc));
                            $('#heHoraInicio').text(horainicio);
                            $('#heHoraTermino').text(horatermino);
                            $('#heVagas').text(vagas);
                            $('#heObservacao').text(observacao);

                            const modal = new bootstrap.Modal(document.getElementById('modalHoraExtra'));
                            modal.show();


                        },
                        error: function () {
                            alert('Erro ao buscar lista.');
                        }
                    });
                });

                $('.btn-aprovar').click(function (e) {
                    const confirmado = confirm("Deseja realmente aprovar essa inscrição?");
                    if (confirmado) {
                        e.preventDefault();
                        const inscritoId = $(this).data('inscrito-id');

                        $.ajax({
                            url: `http://localhost:8080/horaextra/aprovar/${inscritoId}`,
                            method: 'PATCH',
                            contentType: 'application/json',
                            data: JSON.stringify({verificada: true, aprovada: true}),
                            success: function () {
                                carregaAprovaHoraExtras();
                                alert('Hora Extra aprovada com sucesso!');
                            },
                            error: function () {
                                alert('Erro ao aprovar Hora Extra.');
                            }
                        });
                    }
                });


                $('.btn-reprovar').click(function (e) {
                    const confirmado = confirm("Deseja realmente reprovar essa inscrição?");
                    if (confirmado) {
                        e.preventDefault();
                        const inscritoId = $(this).data('inscrito-id');

                        $.ajax({
                            url: `http://localhost:8080/horaextra/aprovar/${inscritoId}`,
                            method: 'PATCH',
                            contentType: 'application/json',
                            data: JSON.stringify({verificada: true, aprovada: false}),
                            success: function () {
                                carregaAprovaHoraExtras();
                                alert('Hora Extra reprovada com sucesso!');
                            },
                            error: function () {
                                alert('Erro ao aprovar Hora Extra.');
                            }
                        });
                    }
                });


            },
            error: function () {
                alert('Erro ao carregar API do Usuário.');
            }
        });

        // Corrige foco após fechar modal para evitar warning do aria-hidden
        $('#modalHoraExtra').on('hide.bs.modal', function () {
            const focused = document.activeElement;
            if ($(this).find(focused).length) {
                focused.blur();
            }
        });

        $('#modalHoraExtra').on('hidden.bs.modal', function () {
            $('body').focus();
        });
    }

    function formatarData(dataISO) {
        if (!dataISO)
            return '';
        const [ano, mes, dia] = dataISO.split('-');
        return `${dia}/${mes}/${ano}`;
    }

    function decimalParaHoraMinutos(totalHoras) {
        const horas = Math.floor(totalHoras);
        const minutos = Math.round((totalHoras - horas) * 60);

        // Adiciona zero à esquerda se precisar
        const horasStr = String(horas).padStart(2, '0');
        const minutosStr = String(minutos).padStart(2, '0');

        return `${horasStr}:${minutosStr}`;
    }

});
