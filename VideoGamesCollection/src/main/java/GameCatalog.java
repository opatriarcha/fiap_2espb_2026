import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GameCatalog.java
 *
 * Aplicação Java Swing com CRUD em memória (Collections) usando DAOs.
 * Domínio: Videogames portáteis (Handhelds) e Jogos (Games).
 * Inclui menus, atalhos de teclado, barra de ferramentas, busca, tabela e diálogos.
 *
 * Compile:  javac GameCatalog.java
 * Execute:  java GameCatalog
 */
public class GameCatalog {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setSystemLookAndFeel();
            new MainFrame().setVisible(true);
        });
    }

    /**
     * Usa o Look and Feel nativo do sistema operacional.
     */
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
    }
}

// ===== DOMAIN =====
class Handheld { // videogame portátil
    private UUID id;
    private String brand;
    private String model;
    private int releaseYear;
    private String batteryHours; // ex.: "3-7h"

    public Handheld(String brand, String model, int releaseYear, String batteryHours) {
        this(UUID.randomUUID(), brand, model, releaseYear, batteryHours);
    }
    public Handheld(UUID id, String brand, String model, int releaseYear, String batteryHours) {
        this.id = id; this.brand = brand; this.model = model; this.releaseYear = releaseYear; this.batteryHours = batteryHours;
    }

    public UUID getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getReleaseYear() { return releaseYear; }
    public String getBatteryHours() { return batteryHours; }

    public void setBrand(String v) { brand = v; }
    public void setModel(String v) { model = v; }
    public void setReleaseYear(int v) { releaseYear = v; }
    public void setBatteryHours(String v) { batteryHours = v; }

    @Override public String toString() { return brand + " " + model + " (" + releaseYear + ")"; }
}

class Game {
    private UUID id;
    private String title;
    private String genre;
    private int year;
    private UUID handheldId; // referência ao portátil

    public Game(String title, String genre, int year, UUID handheldId) {
        this(UUID.randomUUID(), title, genre, year, handheldId);
    }
    public Game(UUID id, String title, String genre, int year, UUID handheldId) {
        this.id = id; this.title = title; this.genre = genre; this.year = year; this.handheldId = handheldId;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public UUID getHandheldId() { return handheldId; }

    public void setTitle(String v) { title = v; }
    public void setGenre(String v) { genre = v; }
    public void setYear(int v) { year = v; }
    public void setHandheldId(UUID v) { handheldId = v; }
}

// ===== DAO (em memória) =====
interface HandheldDAO {
    List<Handheld> listAll();
    Handheld add(Handheld h);
    Handheld update(Handheld h);
    void deleteById(UUID id);
    Optional<Handheld> findById(UUID id);
}

class InMemoryHandheldDAO implements HandheldDAO {
    private final Map<UUID, Handheld> store = new LinkedHashMap<>();
    public InMemoryHandheldDAO() {
        add(new Handheld("Nintendo", "Switch Lite", 2019, "3-7h"));
        add(new Handheld("Valve", "Steam Deck", 2022, "2-8h"));
        add(new Handheld("ASUS", "ROG Ally", 2023, "1.5-4h"));
    }
    @Override public List<Handheld> listAll() { return new ArrayList<>(store.values()); }
    @Override public Handheld add(Handheld h) { store.put(h.getId(), h); return h; }
    @Override public Handheld update(Handheld h) { store.put(h.getId(), h); return h; }
    @Override public void deleteById(UUID id) { store.remove(id); }
    @Override public Optional<Handheld> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
}

interface GameDAO {
    List<Game> listAll();
    List<Game> search(String q, HandheldDAO handheldDAO);
    Game add(Game g);
    Game update(Game g);
    void deleteById(UUID id);
    Optional<Game> findById(UUID id);
}

class InMemoryGameDAO implements GameDAO {
    private final Map<UUID, Game> store = new LinkedHashMap<>();

    public InMemoryGameDAO(HandheldDAO handhelds) {
        // registros de exemplo
        UUID switchLite = handhelds.listAll().stream().filter(h -> h.getModel().contains("Lite")).findFirst().map(Handheld::getId).orElse(null);
        UUID deck = handhelds.listAll().stream().filter(h -> h.getModel().contains("Deck")).findFirst().map(Handheld::getId).orElse(null);
        if (switchLite != null) add(new Game("The Legend of Zelda: Link's Awakening", "Aventura", 2019, switchLite));
        if (switchLite != null) add(new Game("Hades", "Rogue-like", 2020, switchLite));
        if (deck != null) add(new Game("Vampire Survivors", "Ação", 2022, deck));
    }

    @Override public List<Game> listAll() { return new ArrayList<>(store.values()); }

    @Override public List<Game> search(String q, HandheldDAO handheldDAO) {
        String s = q == null ? "" : q.trim().toLowerCase();
        if (s.isEmpty()) return listAll();
        return store.values().stream().filter(g -> {
            boolean basic = g.getTitle().toLowerCase().contains(s)
                    || g.getGenre().toLowerCase().contains(s)
                    || (""+g.getYear()).contains(s);
            boolean onHandheld = handheldDAO.findById(g.getHandheldId())
                    .map(h -> (h.getBrand()+" "+h.getModel()).toLowerCase().contains(s))
                    .orElse(false);
            return basic || onHandheld;
        }).collect(Collectors.toList());
    }

    @Override public Game add(Game g) { store.put(g.getId(), g); return g; }
    @Override public Game update(Game g) { if(!store.containsKey(g.getId())) throw new IllegalArgumentException("Jogo não encontrado"); store.put(g.getId(), g); return g; }
    @Override public void deleteById(UUID id) { store.remove(id); }
    @Override public Optional<Game> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
}

// ===== TABLE MODELS =====
class GameTableModel extends AbstractTableModel {
    private final String[] cols = {"ID", "Título", "Gênero", "Ano", "Portátil"};
    private List<Game> data = new ArrayList<>();
    private final HandheldDAO handheldDAO;

    public GameTableModel(HandheldDAO handheldDAO) { this.handheldDAO = handheldDAO; }

    public void setData(List<Game> games) { this.data = new ArrayList<>(games); fireTableDataChanged(); }
    public Game getAt(int row) { return data.get(row); }
    public int indexOf(UUID id) { 
        for (int i=0;i<data.size();i++) 
            if (data.get(i).getId().equals(id)) 
                return i; 
            return -1; 
        }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return columnIndex==0?UUID.class:(columnIndex==3?Integer.class:String.class); }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Game g = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return g.getId();
            case 1: return g.getTitle();
            case 2: return g.getGenre();
            case 3: return g.getYear();
            case 4:
                return handheldDAO.findById(g.getHandheldId()).map(h -> h.getBrand()+" "+h.getModel()).orElse("-");
            default: return null;
        }
    }
}

class HandheldTableModel extends AbstractTableModel {
    private final String[] cols = {"ID", "Marca", "Modelo", "Lançamento", "Bateria"};
    private List<Handheld> data = new ArrayList<>();
    public void setData(List<Handheld> list) { data = new ArrayList<>(list); fireTableDataChanged(); }
    public Handheld getAt(int row) { return data.get(row); }
    public int indexOf(UUID id) { for(int i=0;i<data.size();i++) if (data.get(i).getId().equals(id)) return i; return -1; }
    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Class<?> getColumnClass(int c) { return c==0?UUID.class:(c==3?Integer.class:String.class); }
    @Override public Object getValueAt(int r, int c) {
        Handheld h = data.get(r);
        switch (c) {
            case 0: return h.getId();
            case 1: return h.getBrand();
            case 2: return h.getModel();
            case 3: return h.getReleaseYear();
            case 4: return h.getBatteryHours();
            default: return null;
        }
    }
}

// ===== UI PRINCIPAL =====
class MainFrame extends JFrame {
    private final HandheldDAO handheldDAO = new InMemoryHandheldDAO();
    private final GameDAO gameDAO = new InMemoryGameDAO(handheldDAO);

    private final GameTableModel gameTableModel = new GameTableModel(handheldDAO);
    private final JTable gameTable = new JTable(gameTableModel);
    private final JTextField searchField = new JTextField();
    private final JLabel status = new JLabel("Pronto.");

    public MainFrame() {
        super("Catálogo – Jogos & Portáteis (CRUD em memória)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 540));
        setLocationByPlatform(true);

        buildMenuBar();
        buildContent();
        installKeyBindings();
        refresh();
    }

    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("Arquivo");
        JMenuItem miExit = new JMenuItem("Sair");
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        miExit.addActionListener(e -> dispose());
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

        bar.add(file); bar.add(games); bar.add(handhelds); bar.add(help);
        setJMenuBar(bar);
    }

    private void buildContent() {
        // Toolbar
        JToolBar tb = new JToolBar(); tb.setFloatable(false); tb.setRollover(true);
        JButton bNew = new JButton("Novo Jogo"); bNew.addActionListener(e -> onNewGame());
        JButton bEdit = new JButton("Editar"); bEdit.addActionListener(e -> onEditSelectedGame());
        JButton bDel = new JButton("Excluir"); bDel.addActionListener(e -> onDeleteSelectedGames());
        JButton bHH  = new JButton("Portáteis…"); bHH.addActionListener(e -> onManageHandhelds());
        tb.add(bNew); tb.add(bEdit); tb.add(bDel); tb.addSeparator(); tb.add(bHH);

        // Busca
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(8,8,4,8));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.add(new JLabel("Buscar:"));
        searchField.setPreferredSize(new Dimension(320, 28));
        left.add(searchField);
        JButton btnClear = new JButton("Limpar"); btnClear.addActionListener(e -> searchField.setText(""));
        top.add(left, BorderLayout.CENTER); top.add(btnClear, BorderLayout.EAST);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applySearch(); }
            @Override public void removeUpdate(DocumentEvent e) { applySearch(); }
            @Override public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        // Tabela
        gameTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gameTable.setAutoCreateRowSorter(true);
        gameTable.setFillsViewportHeight(true);
        gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        gameTable.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(gameTable);

        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        statusBar.add(status, BorderLayout.WEST);

        // Layout root
        JPanel center = new JPanel(new BorderLayout());
        center.add(tb, BorderLayout.NORTH);
        center.add(top, BorderLayout.CENTER);
        center.add(scroll, BorderLayout.SOUTH);

        JPanel root = new JPanel(new BorderLayout());
        root.add(center, BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);
        add(root);
    }

    private void installKeyBindings() {
        InputMap im = gameTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = gameTable.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "editRow");
        am.put("editRow", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { onEditSelectedGame(); }});
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRows");
        am.put("deleteRows", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { onDeleteSelectedGames(); }});
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "newRow");
        am.put("newRow", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { onNewGame(); }});
    }

    private void refresh() {
        gameTableModel.setData(gameDAO.listAll());
        updateStatus();
    }

    private void applySearch() {
        gameTableModel.setData(gameDAO.search(searchField.getText(), handheldDAO));
        updateStatus();
    }

    private void updateStatus() {
        status.setText(String.format("Jogos: %d | Portáteis: %d", gameTableModel.getRowCount(), handheldDAO.listAll().size()));
    }

    // ==== Ações Jogos ====
    private void onNewGame() {
        GameDialog dlg = new GameDialog(this, null, handheldDAO);
        dlg.setVisible(true);
        dlg.getResult().ifPresent(g -> { gameDAO.add(g); applySearch(); selectGame(g.getId()); });
    }
    private void onEditSelectedGame() {
        int vrow = gameTable.getSelectedRow(); if (vrow < 0) { warn("Selecione um jogo para editar."); return; }
        int mrow = gameTable.convertRowIndexToModel(vrow);
        Game g = gameTableModel.getAt(mrow);
        GameDialog dlg = new GameDialog(this, g, handheldDAO);
        dlg.setVisible(true);
        dlg.getResult().ifPresent(updated -> { gameDAO.update(updated); applySearch(); selectGame(updated.getId()); });
    }
    private void onDeleteSelectedGames() {
        int[] vrows = gameTable.getSelectedRows(); if (vrows.length==0) { warn("Selecione um ou mais jogos para excluir."); return; }
        if (JOptionPane.showConfirmDialog(this, "Confirma exclusão?", "Excluir", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        for (int i=vrows.length-1; i>=0; i--) {
            int mrow = gameTable.convertRowIndexToModel(vrows[i]);
            UUID id = gameTableModel.getAt(mrow).getId();
            gameDAO.deleteById(id);
        }
        applySearch();
    }
    private void selectGame(UUID id) {
        int m = gameTableModel.indexOf(id); if (m>=0) { int v = gameTable.convertRowIndexToView(m); gameTable.getSelectionModel().setSelectionInterval(v, v); gameTable.scrollRectToVisible(gameTable.getCellRect(v, 0, true)); }
    }

    // ==== Portáteis ====
    private void onManageHandhelds() {
        HandheldManagerDialog dlg = new HandheldManagerDialog(this, handheldDAO, gameDAO);
        dlg.setVisible(true);
        applySearch(); // nomes podem refletir na coluna "Portátil"
    }

    private void warn(String msg) { JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE); }
}

// ===== DIALOGOS =====
class GameDialog extends JDialog {
    private final JTextField tfTitle = new JTextField();
    private final JTextField tfGenre = new JTextField();
    private final JSpinner spYear = new JSpinner(new SpinnerNumberModel(2024, 1970, 2100, 1));
    private final JComboBox<Handheld> cbHandhelds;
    private UUID id;
    private Optional<Game> result = Optional.empty();

    public GameDialog(Window owner, Game existing, HandheldDAO handheldDAO) {
        super(owner, existing==null?"Novo Jogo":"Editar Jogo", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 260); setLocationRelativeTo(owner);
        cbHandhelds = new JComboBox<>(handheldDAO.listAll().toArray(new Handheld[0]));
        buildForm();
        if (existing != null) fill(existing, handheldDAO);
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4); c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1.0;
        int r=0;
        c.gridx=0;c.gridy=r; form.add(new JLabel("Título:"), c); c.gridx=1; form.add(tfTitle, c); r++;
        c.gridx=0;c.gridy=r; form.add(new JLabel("Gênero:"), c); c.gridx=1; form.add(tfGenre, c); r++;
        c.gridx=0;c.gridy=r; form.add(new JLabel("Ano:"), c); c.gridx=1; form.add(spYear, c); r++;
        c.gridx=0;c.gridy=r; form.add(new JLabel("Portátil:"), c); c.gridx=1; form.add(cbHandhelds, c); r++;

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btOk = new JButton("OK"); JButton btCancel = new JButton("Cancelar");
        btOk.addActionListener(e -> onOK()); btCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btOk);
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttons.add(btOk); buttons.add(btCancel);

        add(form, BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void fill(Game g, HandheldDAO handheldDAO) {
        id = g.getId(); tfTitle.setText(g.getTitle()); tfGenre.setText(g.getGenre()); spYear.setValue(g.getYear());
        handheldDAO.findById(g.getHandheldId()).ifPresent(h -> cbHandhelds.setSelectedItem(h));
    }

    private void onOK() {
        String t = tfTitle.getText().trim(); if (t.isEmpty()) { warn("Título é obrigatório."); tfTitle.requestFocus(); return; }
        String g = tfGenre.getText().trim(); if (g.isEmpty()) { warn("Gênero é obrigatório."); tfGenre.requestFocus(); return; }
        int y = (Integer) spYear.getValue();
        Handheld h = (Handheld) cbHandhelds.getSelectedItem(); if (h == null) { warn("Selecione um portátil."); return; }
        Game game = (id==null) ? new Game(t, g, y, h.getId()) : new Game(id, t, g, y, h.getId());
        result = Optional.of(game); dispose();
    }

    private void warn(String msg) { JOptionPane.showMessageDialog(this, msg, "Validação", JOptionPane.WARNING_MESSAGE); }
    public Optional<Game> getResult() { return result; }
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
            boolean inUse = gameDAO.listAll().stream().anyMatch(g -> id.equals(g.getHandheldId()));
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
