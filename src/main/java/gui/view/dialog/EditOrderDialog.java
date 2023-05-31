package gui.view.dialog;

import database.model.Order;
import database.model.OrderLine;
import gui.ViewBuilder;
import gui.controller.EditOrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditOrderDialog extends JDialog implements ViewBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EditOrderDialog.class);
    public static Order order;

    private final EditOrderController editOrderController;
    private JLabel header;
    private JPanel scrollablePanel;
    private Order copy;

    public EditOrderDialog() {
        this.editOrderController = new EditOrderController();
        buildAndShowView();
    }

    @Override
    public void buildAndShowView() {
        this.setModal(true);
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(450, 280));
        this.setMaximumSize(new Dimension(425, 280));
        this.setLocationRelativeTo(null);

        header = new JLabel("", SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        this.add(header, BorderLayout.NORTH);

        // make a scrollable panel
        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
        scrollablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        this.copy = order.copy();
        updateView();
    }

    private void updateView() {
        this.header.setText(String.format("Order %d bewerken", order.getOrderId()));
        this.scrollablePanel.removeAll();

        JPanel orderLinePanelHeader = new JPanel();
        orderLinePanelHeader.setMaximumSize(new Dimension(450, 30));
        orderLinePanelHeader.setLayout(new GridLayout(1, 3));
        orderLinePanelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        orderLinePanelHeader.add(new JLabel("Product"));
        orderLinePanelHeader.add(new JLabel("Aantal"));
        orderLinePanelHeader.add(new JLabel("Gepickt Aantal"));
        orderLinePanelHeader.add(new JLabel("Verwijderen"));

        // add the header above the scrollpane
        this.scrollablePanel.add(orderLinePanelHeader);

        List<OrderLine> orderLines = copy.getOrderLines();
        orderLines.forEach(orderLine -> {
            JPanel orderLinePanel = new JPanel();
            orderLinePanel.setMaximumSize(new Dimension(400, 30));
            orderLinePanel.setLayout(new GridLayout(1, 4));
            orderLinePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

            JLabel productLabel = new JLabel(orderLine.getDescription());
            productLabel.setPreferredSize(new Dimension(100, 30));
            productLabel.setToolTipText(orderLine.getDescription());

            orderLinePanel.add(productLabel);

            JTextField quantityField = new JTextField(String.valueOf(orderLine.getQuantity()));
            orderLinePanel.add(quantityField);

            JTextField pickedQuantityField = new JTextField(String.valueOf(orderLine.getPickedQuantity()));
            orderLinePanel.add(pickedQuantityField);

            JButton deleteButton = new JButton("Verwijder");
            deleteButton.addActionListener(e -> {
                orderLines.remove(orderLine);
                updateView();
            });
            orderLinePanel.add(deleteButton);

            scrollablePanel.add(orderLinePanel);
        });

        JPanel editOrderBottomBar = new JPanel();
        editOrderBottomBar.setLayout(new GridLayout(1, 2));

        JButton saveButton = new JButton("Opslaan");
        JButton cancelButton = new JButton("Annuleren");
        cancelButton.addActionListener(e -> editOrderController.cancelButton(this));

        editOrderBottomBar.add(saveButton);
        editOrderBottomBar.add(cancelButton);

        this.add(editOrderBottomBar, BorderLayout.SOUTH);
    }
}