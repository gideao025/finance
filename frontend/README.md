# Finance Dashboard - Frontend

Um dashboard financeiro moderno e responsivo construído com Angular, inspirado no design Hope UI.

## 🚀 Tecnologias

- **Angular 15+** - Framework principal
- **Angular Material** - Componentes de UI
- **Lucide Angular** - Ícones
- **ng2-charts** - Gráficos
- **SCSS** - Estilização
- **TypeScript** - Linguagem de programação

## 📱 Funcionalidades

### Dashboard Principal
- Visão geral das finanças
- Gráficos de gastos e receitas
- Lista de transações recentes
- Cards de metas financeiras
- Estatísticas em tempo real

### Páginas Específicas
- **Transações**: Gerenciamento completo de transações
- **Cartões**: Visualização e controle de cartões de crédito/débito
- **Vales**: Gestão de vales e benefícios

### Características Técnicas
- Design responsivo (mobile-first)
- Tema inspirado no Hope UI
- Navegação intuitiva com sidebar
- Componentes reutilizáveis
- Serviço de responsividade integrado

## 🎨 Design System

### Cores
- **Primary**: Azul (#3B82F6)
- **Secondary**: Cinza (#6B7280)
- **Success**: Verde (#10B981)
- **Warning**: Amarelo (#F59E0B)
- **Error**: Vermelho (#EF4444)

### Breakpoints
- **Mobile**: < 768px
- **Tablet**: 768px - 1023px
- **Desktop**: ≥ 1024px

### Componentes
- `StatsCardComponent` - Cards de estatísticas
- `ExpenseChartComponent` - Gráficos de gastos
- `TransactionListComponent` - Lista de transações
- `GoalsCardComponent` - Cards de metas

## 📁 Estrutura do Projeto

```
src/
├── app/
│   ├── dashboard/              # Página principal
│   │   └── components/         # Componentes do dashboard
│   ├── transactions/           # Página de transações
│   ├── cards/                  # Página de cartões
│   ├── vouchers/              # Página de vales
│   ├── shared/                # Serviços compartilhados
│   │   └── services/          # Serviços (responsividade, etc.)
│   ├── app-routing.module.ts  # Configuração de rotas
│   └── app.module.ts          # Módulo principal
├── styles/                    # Estilos globais
│   ├── _variables.scss        # Variáveis CSS
│   ├── _mixins.scss          # Mixins SCSS
│   ├── _theme.scss           # Tema principal
│   └── responsive.scss       # Utilitários responsivos
└── assets/                   # Recursos estáticos
```

## 🛠️ Instalação e Execução

### Pré-requisitos
- Node.js 16+
- npm ou yarn

### Passos

1. **Instalar dependências**
   ```bash
   npm install
   ```

2. **Executar em desenvolvimento**
   ```bash
   ng serve
   ```

3. **Build para produção**
   ```bash
   ng build --prod
   ```

4. **Executar testes**
   ```bash
   ng test
   ```

## 📱 Responsividade

O projeto foi desenvolvido com abordagem mobile-first e inclui:

- Sidebar adaptável (overlay em mobile, side em desktop)
- Grid responsivo para cards e componentes
- Tipografia escalável
- Espaçamentos adaptativos
- Componentes otimizados para touch

### Serviço de Responsividade

```typescript
// Exemplo de uso
constructor(private responsiveService: ResponsiveService) {}

ngOnInit() {
  this.responsiveService.isMobile$.subscribe(isMobile => {
    // Lógica responsiva
  });
}
```

## 🎯 Próximos Passos

- [ ] Integração com backend
- [ ] Autenticação e autorização
- [ ] Testes unitários e e2e
- [ ] PWA (Progressive Web App)
- [ ] Internacionalização (i18n)
- [ ] Modo escuro
- [ ] Notificações push

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Contato

Para dúvidas ou sugestões, entre em contato através dos issues do GitHub.