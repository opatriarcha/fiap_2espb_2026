package br.com.fiap.espb.ddd;

import lombok.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GamesCatalog {

    public static void main(String[] args) {
//            initAllFuckingThings();
        SwingUtilities.invokeLater(() -> {
            setSystemLookAndFeel();
            JFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    private void initAllFuckingThings() {
        new Starter().start();
    }

    class Starter {
        public void start() {
            SwingUtilities.invokeLater(() -> {
                setSystemLookAndFeel();
                new MainFrame().setVisible(true);
            });
        }
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
class Handheld {
    private @Setter
    @Getter UUID id;
    private @Setter
    @Getter String brand;
    private @Setter
    @Getter String model;
    private @Setter
    @Getter Integer releaseYear;
    private @Setter
    @Getter String batteryHours;

    public Handheld(String brand, String model, Integer releaseYear, String batteryHours) {
        this.brand = brand;
        this.model = model;
        this.releaseYear = releaseYear;
        this.batteryHours = batteryHours;
    }
}

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
class Game {
    private @Setter
    @Getter UUID id;
    private @Setter
    @Getter String title;
    private @Setter
    @Getter String genre;
    private @Setter
    @Getter Integer year;
    private @Setter
    @Getter Handheld handheld;
}
    interface GameDAO{
        List<Game> listAll();
        List<Game> search(String query, HandheldDAO handheldDAO);
        Game add( Game game);
        Game update(Game game);
        void delete(Game game);
        void deleteById(UUID id);
        Optional<Game> findById(UUID id);
    }

    class InMemoryGameDAO implements GameDAO{

        private final Map<UUID, Game> store = new LinkedHashMap<>();

        @Override
        public List<Game> listAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public List<Game> search(String query, HandheldDAO handheldDAO) {
            String s = (query == null ? "" : query.trim().toLowerCase());

            if(s.isEmpty())
                return listAll();

            return store.values().stream().filter(g -> {
                //Query filter dos jogos
                boolean basic = g.getTitle().toLowerCase().contains(s)
                    || g.getGenre().toLowerCase().contains(s)
                    || ("" + g.getYear()).toLowerCase().contains(s);


                //Query filter dos videogames
                boolean onHandheld = handheldDAO.findById(g.getId())
                        .map(h ->
                                ( h.getBrand() + " " + h.getModel()).toLowerCase().contains(s)
                        ).orElse(false);
                return basic || onHandheld;
            }).collect(Collectors.toUnmodifiableList());
        }

        @Override
        public Game add(Game game) {
            return store.put(game.getId(), game);
        }

        @Override
        public Game update(Game game) {
            return store.replace(game.getId(), game);
        }

        @Override
        public void delete(Game game) {
            deleteById(game.getId());
        }

        @Override
        public void deleteById(UUID id) {
            store.remove(id);
        }

        @Override
        public Optional<Game> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }
    }

interface HandheldDAO{
    List<Handheld> listAll();
    Handheld add(Handheld handheld);
    Handheld update(Handheld handheld);
    void deleteById(UUID id);
    Optional<Handheld> findById(UUID id);
}

class InMemoryHandheldDAO implements HandheldDAO{
        private final Map<UUID, Handheld> store = new HashMap<>();

        public InMemoryHandheldDAO(){
            // Consoles da Nintendo
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game & Watch", 1980, "Variável por modelo, geralmente 1-2 pilhas tipo LR44")); // Baseado em pilhas tipo botão
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy (Classic)", 1989, "aprox. 10-14h (4 pilhas AA)")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Pocket", 1996, "aprox. 10h (2 pilhas AAA)")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Light", 1998, "aprox. 12h (luz ligada) / 20h (luz desligada) (2 pilhas AA)")); // Versão japonesa
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Color", 1998, "aprox. 10h (2 pilhas AA)")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Advance", 2001, "aprox. 15h (2 pilhas AA)")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Advance SP", 2003, "aprox. 10h (luz ligada) / 18h (luz desligada)")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Game Boy Micro", 2005, "aprox. 5-8h")); //
            add(new Handheld(UUID.randomUUID(), "Nintendo", "DS (Original)", 2004, "aprox. 4-10h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "DS Lite", 2006, "aprox. 5-19h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "DSi", 2008, "aprox. 3-14h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "3DS (Original)", 2011, "aprox. 3-5h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "2DS", 2013, "aprox. 3.5-5.5h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "3DS XL (Original)", 2012, "aprox. 3.5-6.5h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "New 3DS XL", 2015, "aprox. 3.5-7h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Switch", 2017, "aprox. 2.5-6.5h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Switch (revisão com melhor bateria)", 2019, "aprox. 4.5-9h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Switch Lite", 2019, "aprox. 3-7h"));
            add(new Handheld(UUID.randomUUID(), "Nintendo", "Switch OLED", 2021, "aprox. 4.5-9h"));

            // Consoles da Atari
            add(new Handheld(UUID.randomUUID(), "Atari", "Lynx", 1989, "aprox. 4-5h (6 pilhas AA)")); //
            add(new Handheld(UUID.randomUUID(), "Atari", "Lynx II", 1991, "aprox. 4-5h (6 pilhas AA)")); // Revisão do Lynx original

            // Consoles da Sega
            add(new Handheld(UUID.randomUUID(), "Sega", "Game Gear", 1990, "aprox. 3-5h (6 pilhas AA)")); //
            add(new Handheld(UUID.randomUUID(), "Sega", "Genesis Nomad", 1995, "aprox. 2-4h (6 pilhas AA)")); // Baseado no Mega Drive

            // Consoles da Bandai
            add(new Handheld(UUID.randomUUID(), "Bandai", "Playdia", 1994, "Não é portátil, console de mesa")); // Removido, pois não é portátil
            add(new Handheld(UUID.randomUUID(), "Bandai", "Wonderswan (Mono)", 1999, "aprox. 30-40h (1 pilha AA)"));
            add(new Handheld(UUID.randomUUID(), "Bandai", "Wonderswan Color", 2000, "aprox. 20h (1 pilha AA)"));
            add(new Handheld(UUID.randomUUID(), "Bandai", "Wonderswan Crystal", 2002, "aprox. 15h (1 pilha AA)"));

        }


        @Override
        public List<Handheld> listAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Handheld add(Handheld handheld) {
            return store.put(handheld.getId(), handheld);
        }

        @Override
        public Handheld update(Handheld handheld) {
            return store.replace(handheld.getId(), handheld);
        }

        @Override
        public void deleteById(UUID id) {
            store.remove(id);
        }

        @Override
        public Optional<Handheld> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }
}

    @RequiredArgsConstructor
    class GameTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Título", "Gênero", "Ano", "Portátil"};
        private @Setter List<Game> data = new ArrayList<>();
        private final HandheldDAO handheldDAO;

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Game game = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> game.getId();
                case 1 -> game.getTitle();
                case 2 -> game.getGenre();
                case 3 -> game.getYear();
                case 4 -> handheldDAO.findById(game.getId()).map(
                                h -> h.getBrand() + " " + h.getModel())
                        .orElse("-");
                default -> null;
            };
        }

        public Game getAt(int row) {
            return data.get(row);
        }

        public int indexOf( UUID id){
            for(int i = 0 ; i < data.size() ; i++){
                if(data.get(i).getId().equals(id))
                    return i;
            }
            return -1;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 0)
                return UUID.class;
            if(columnIndex == 3)
                return Integer.class;
            else
                return String.class;
//                return columnIndex == 0 ? UUID.class:(columnIndex==3 ? Integer.class : String.class);
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }
    }

    @RequiredArgsConstructor
    class HandheldTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Marca", "Modelo", "Lançamento", "Bateria"};
        private @Setter List<Handheld> data = new ArrayList<>();

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 0)
                return UUID.class;
            if(columnIndex == 3)
                return Integer.class;
            else
                return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Handheld h = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> h.getId();
                case 1 -> h.getBrand();
                case 2 -> h.getModel();
                case 3 -> h.getReleaseYear();
                case 4 -> h.getBatteryHours();
                default -> null;
            };
        }

        public int indexOf(UUID id){
            for(int i = 0 ; i < data.size() ; i++){
                if(data.get(i).getId().equals(id))
                    return i;
            }
            return -1;
        }

        public Handheld getAt(int row) {
            return data.get(row);
        }
    }


class MainFrame extends JFrame{
    private final HandheldDAO handheldDAO = new InMemoryHandheldDAO();
    private final GameDAO gameDAO = new InMemoryGameDAO();

    private final GameTableModel gameTableModel = new GameTableModel(new InMemoryHandheldDAO());
    private final JTable gameTable = new JTable(gameTableModel);
    private final JTextField searchField = new JTextField();
    private final JLabel status = new JLabel("Pronto!");

    public MainFrame(){
        super("Mobile Games Catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 540));
        setLocationByPlatform(true);
         buildMenu();
         buildContent();
         installKeyBindings();
//                refresh();
    }

    private void installKeyBindings() {
        InputMap im = gameTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//MOSTRAR EM TELA O COMPORTAM<ENMTO
        ActionMap am = gameTable.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "editRow");
        am.put("editRow", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onEditSelectedGame();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),"deleteRows" );
        am.put("deleteRows", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteSelectedGame();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0),"newRow" );
        am.put("newRow", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onNewGame();
            }
        });

    }

    private void buildMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Arquivo");
        JMenuItem miExit = new JMenuItem("Sair");
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        file.add(miExit);

        JMenu games = new JMenu("Jogos");
        JMenuItem miNew = new JMenuItem("Novo…");
        miNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        miNew.addActionListener(e -> onNewGame());
        JMenuItem miEdit = new JMenuItem("Editar…");
        miEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        miEdit.addActionListener(e -> onEditSelectedGame());
        JMenuItem miDelete = new JMenuItem("Excluir");
        miDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        miDelete.addActionListener(e -> onDeleteSelectedGames());
        JMenuItem miFind = new JMenuItem("Localizar");
        miFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        miFind.addActionListener(e -> { searchField.requestFocusInWindow(); searchField.selectAll(); });

        games.add(miNew); games.add(miEdit); games.add(miDelete); games.addSeparator(); games.add(miFind);

        JMenu handhelds = new JMenu("Portáteis");
        JMenuItem miManageHandhelds = new JMenuItem("Gerenciar Portáteis…");
        miManageHandhelds.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        miManageHandhelds.addActionListener(e -> onManageHandhelds());
        handhelds.add(miManageHandhelds);

        JMenu help = new JMenu("Ajuda");
        JMenuItem miAbout = new JMenuItem("Sobre");
        miAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        miAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Catálogo de Jogos & Portáteis \r\n CRUD em memória com Swing (L&F do sistema) \r\n© 2025",
                "Sobre", JOptionPane.INFORMATION_MESSAGE));
        help.add(miAbout);

        menuBar.add(file);
        menuBar.add(games);
        menuBar.add(handhelds);
        menuBar.add(help);
        setJMenuBar(menuBar);


    }

    private void onDeleteSelectedGames() {
    }

    private void buildContent() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JButton bNew = new JButton("Novo Jogo");
        bNew.addActionListener(e -> onNewGame()
        );

        JButton bEdit = new JButton("Editar");
        bNew.addActionListener(e -> onEditSelectedGame()
        );

        JButton bDel = new JButton("Excluir");
        bNew.addActionListener(e -> onEditSelectedGames()
        );

        JButton bHH = new JButton("Portáteis...");
        bNew.addActionListener(e -> onManageHandhelds()
        );

        toolBar.add(bNew);
        toolBar.add(bEdit);
        toolBar.add(bDel);
        toolBar.add(bHH);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JPanel left = new JPanel( new FlowLayout(FlowLayout.LEFT, 8, 8));
        left.add( new JLabel("Buscar: "));
        searchField.setPreferredSize(new Dimension(320, 28));
        left.add( searchField );
        JButton btnClear = new JButton("Limpar");
        btnClear.addActionListener(e -> {searchField.setText("");});
        top.add(btnClear, BorderLayout.EAST);
        top.add(left, BorderLayout.WEST);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearch();
            }
        });

        gameTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gameTable.setAutoCreateRowSorter(true);
        gameTable.setFillsViewportHeight(true);
        gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        gameTable.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(gameTable);

        JPanel statusBar =  new JPanel( new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        statusBar.add(status, BorderLayout.WEST);


        JPanel center = new JPanel( new BorderLayout());
        center.add(toolBar, BorderLayout.NORTH);
        center.add(top, BorderLayout.CENTER);
        center.add(scrollPane, BorderLayout.SOUTH);

        JPanel root =  new JPanel(new BorderLayout());
        root.add(center, BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);

        add(root);
    }

    private void applySearch() {
        gameTableModel.setData(gameDAO.search(searchField.getText(), handheldDAO));
    }

    private void refresh() {
        gameTableModel.setData(gameDAO.listAll());
    }

    private void onManageHandhelds() {
        GameDialog.HandheldManagerDialog handheldManagerDialog = new GameDialog.HandheldManagerDialog(null, handheldDAO, gameDAO);
    }

    private void onEditSelectedGames() {

    }

    private void onEditSelectedGame() {
        int vrow = gameTable.getSelectedRow();
        if (vrow != -1) {
            warn( "Selecione pelo menos um jogo pra Editar.");
            return;
        }
        int mrow = gameTable.convertColumnIndexToModel(vrow);
        Game g = gameTableModel.getAt(mrow);
        GameDialog dlg = new GameDialog(this, g, handheldDAO);
        dlg.setVisible(true);
        dlg.getResult().ifPresent(updated -> {
            gameDAO.update(updated);
            applySearch();
            selectGame(updated.getId());
        });
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void selectGame(UUID id) {
        int m = gameTableModel.indexOf(id);
        if (m>=0) {
            int v = gameTable.convertRowIndexToView(m);
            gameTable.getSelectionModel().setSelectionInterval(v, v);
            gameTable.scrollRectToVisible(gameTable.getCellRect(v, 0, true));
        }

    }

        private void onNewGame() {
        GameDialog dlg = new GameDialog(this, null, handheldDAO);
    }

    private void onDeleteSelectedGame(){
        int[] vrows = gameTable.getSelectedRows();
        if(JOptionPane.showConfirmDialog(this, "Confirma a exclusao?", "Excluir",  JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        for(int i=vrows.length -1; i>=0; i--) {
            int mrow = gameTable.convertRowIndexToModel(vrows[i]);
            UUID id = gameTableModel.getAt(mrow).getId();
            gameDAO.deleteById(id);
        }
        applySearch();
    }

    private void updateStatus() {
        status.setText(String.format("Jogos: %d | Portáteis %d", gameTable.getRowCount(), handheldDAO.listAll().size()));
    }
}

class GameDialog extends JDialog{
    private final JTextField tfTitle = new JTextField();
    private final JTextField tfGenre = new JTextField();
    private final JSpinner spYear = new JSpinner(new SpinnerNumberModel(2025, 1970, 2026, 1));
    private JComboBox<Handheld> cbHandhelds;
    private UUID id;
    private Optional<Game> result = Optional.empty();

    public GameDialog(Window owner, Game existing, HandheldDAO handheldDAO) {
        super(owner, existing == null ? "Novo Jogo" : "Editar Jogo", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 260);
        cbHandhelds = new JComboBox<>(handheldDAO.listAll().toArray( new Handheld[0]));
        buildForm();
        if( existing != null )
            fill(existing, handheldDAO);
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int r = 0;
        c.gridx = 0;
        c.gridy = r;
        form.add(new JLabel("Título: "), c);
        c.gridx = 1;
        form.add(tfTitle, c);
        r++;
        c.gridx = 0;
        c.gridy = r;
        form.add(new JLabel("Gênero: "), c);
        c.gridx = 1;
        form.add(tfGenre, c);
        r++;
        c.gridx = 0;
        c.gridy = r;
        form.add(new JLabel("Ano: "), c);
        c.gridx = 1;
        form.add(spYear, c);
        r++;
        c.gridx = 0;
        c.gridy = r;
        form.add(new JLabel("Portatil: "), c);
        c.gridx = 1;
        form.add(cbHandhelds, c);
        r++;

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btOk = new JButton("OK");
        JButton brCancel = new JButton("Cancelar");
        btOk.addActionListener(e -> {
            onOk();
        });
        brCancel.addActionListener(e -> {
            dispose();
        });

        getRootPane().setDefaultButton(btOk);
        getRootPane().registerKeyboardAction(e ->
                        dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttons.add(btOk);
        buttons.add(brCancel);
    }

    private void fill(Game g, HandheldDAO handheldDAO) {
        id = g.getId();
        tfTitle.setText(g.getTitle());
        tfGenre.setText(g.getGenre());
        spYear.setValue(g.getYear());
        handheldDAO.findById(g.getHandheld().getId()).ifPresent(h -> { cbHandhelds.setSelectedItem(h); });
    }

    private void onOk() {
        String t =  tfTitle.getText().trim();
        if( t.isEmpty() ) {
            warn("Titulo é Obrigatório!");
            tfTitle.requestFocus();
            return;
        }
        String g = tfGenre.getText().trim();
        if( g.isEmpty() ) {
            warn("Genero é Obrigatório!");
            tfGenre.requestFocus();
            return;
        }

        int y = (int)spYear.getValue();

        Handheld h  = (Handheld) cbHandhelds.getSelectedItem();
         if( h == null ) {
             warn("Selecione um portátil!");
             cbHandhelds.requestFocus();
             return;
         }

         Game game = game = new Game(Objects.requireNonNullElseGet(id, UUID::randomUUID), t, g, y, h);

         result = Optional.of(game);
         dispose();



    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public Optional<Game> getResult() {
        return result;
    }


    class HandheldDialog extends JDialog {
        private final JTextField tfBrand = new JTextField();
        private final JTextField tfModel = new JTextField();
        private final JSpinner spYear = new JSpinner(new SpinnerNumberModel(2022, 1990, 2100, 1));
        private final JTextField tfBattery = new JTextField();
        private UUID id;
        private Optional<Handheld> result = Optional.empty();

    public HandheldDialog(Window owner, Handheld existing) {
            super(owner, existing==null?"Novo Portátil":"Editar Portátil", ModalityType.APPLICATION_MODAL);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(520, 240); setLocationRelativeTo(owner);
            buildForm(); if (existing!=null) fill(existing);
        }

        private void buildForm() {
            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(4,4,4,4); c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1.0;
            int r=0;
            c.gridx=0;c.gridy=r; form.add(new JLabel("Marca:"), c); c.gridx=1; form.add(tfBrand, c); r++;
            c.gridx=0;c.gridy=r; form.add(new JLabel("Modelo:"), c); c.gridx=1; form.add(tfModel, c); r++;
            c.gridx=0;c.gridy=r; form.add(new JLabel("Lançamento:"), c); c.gridx=1; form.add(spYear, c); r++;
            c.gridx=0;c.gridy=r; form.add(new JLabel("Bateria:"), c); c.gridx=1; form.add(tfBattery, c); r++;

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btOk = new JButton("OK"); JButton btCancel = new JButton("Cancelar");
            btOk.addActionListener(e -> onOK()); btCancel.addActionListener(e -> dispose());
            getRootPane().setDefaultButton(btOk);
            getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            buttons.add(btOk); buttons.add(btCancel);

            add(form, BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
        }

        private void fill(Handheld h) { id = h.getId(); tfBrand.setText(h.getBrand()); tfModel.setText(h.getModel()); spYear.setValue(h.getReleaseYear()); tfBattery.setText(h.getBatteryHours()); }

        private void onOK() {
            String b = tfBrand.getText().trim(); if (b.isEmpty()) { warn("Marca é obrigatória."); tfBrand.requestFocus(); return; }
            String m = tfModel.getText().trim(); if (m.isEmpty()) { warn("Modelo é obrigatório."); tfModel.requestFocus(); return; }
            int y = (Integer) spYear.getValue();
            String bat = tfBattery.getText().trim(); if (bat.isEmpty()) bat = "-";
            Handheld h = (id==null) ? new Handheld(b, m, y, bat) : new Handheld(id, b, m, y, bat);
            result = Optional.of(h); dispose();
        }

        private void warn(String msg) { JOptionPane.showMessageDialog(this, msg, "Validação", JOptionPane.WARNING_MESSAGE); }
        public Optional<Handheld> getResult() { return result; }
    }

    class HandheldManagerDialog extends JDialog {
        private final HandheldDAO handheldDAO; private final GameDAO gameDAO;
        private final HandheldTableModel model = new HandheldTableModel();
        private final JTable table = new JTable(model);

        public HandheldManagerDialog(Window owner, HandheldDAO handheldDAO, GameDAO gameDAO) {
            super(owner, "Gerenciar Portáteis", ModalityType.APPLICATION_MODAL);
            this.handheldDAO = handheldDAO; this.gameDAO = gameDAO;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(720, 420); setLocationRelativeTo(owner);
            buildUI(); refresh();
        }

        private void buildUI() {
            JToolBar tb = new JToolBar(); tb.setFloatable(false); tb.setRollover(true);
            JButton bNew = new JButton("Novo"); bNew.addActionListener(e -> onNew());
            JButton bEdit = new JButton("Editar"); bEdit.addActionListener(e -> onEdit());
            JButton bDel = new JButton("Excluir"); bDel.addActionListener(e -> onDelete());
            tb.add(bNew); tb.add(bEdit); tb.add(bDel);

            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            table.setAutoCreateRowSorter(true); table.setRowHeight(22);
            JScrollPane sp = new JScrollPane(table);

            add(tb, BorderLayout.NORTH); add(sp, BorderLayout.CENTER);
        }

        private void refresh() { model.setData(handheldDAO.listAll()); }

        private void onNew() {
            HandheldDialog dlg = new HandheldDialog(this, null);
            dlg.setVisible(true);
            dlg.getResult().ifPresent(h -> { handheldDAO.add(h); refresh(); });
        }
        private void onEdit() {
            int vr = table.getSelectedRow(); if (vr<0) { warn("Selecione um portátil para editar."); return; }
            int mr = table.convertRowIndexToModel(vr);
            Handheld h = model.getAt(mr);
            HandheldDialog dlg = new HandheldDialog(this, h);
            dlg.setVisible(true);
            dlg.getResult().ifPresent(upd -> { handheldDAO.update(upd); refresh(); });
        }
        private void onDelete() {
            int[] vrows = table.getSelectedRows(); if (vrows.length==0) { warn("Selecione um ou mais portáteis para excluir."); return; }
            // Verifica jogos vinculados
            for (int vr : vrows) {
                int mr = table.convertRowIndexToModel(vr);
                UUID id = model.getAt(mr).getId();
                boolean inUse = gameDAO.listAll().stream().anyMatch(g -> id.equals(g.getHandheld().getId()));
                if (inUse) { warn("Há jogos vinculados a este portátil. Remova/edite os jogos antes de excluir."); return; }
            }
            if (JOptionPane.showConfirmDialog(this, "Confirma exclusão?", "Excluir", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            for (int i=vrows.length-1;i>=0;i--) {
                int mr = table.convertRowIndexToModel(vrows[i]);
                UUID id = model.getAt(mr).getId(); handheldDAO.deleteById(id);
            }
            refresh();
        }
        private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    }



}

