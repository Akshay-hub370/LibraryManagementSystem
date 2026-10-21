package com.library.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class UIUtils {

    // ================= COLORS =================

    public static final Color SIDEBAR =
            new Color(35, 47, 62);

    public static final Color PRIMARY =
            new Color(52, 152, 219);

    public static final Color SUCCESS =
            new Color(46, 204, 113);

    public static final Color DANGER =
            new Color(231, 76, 60);

    public static final Color WARNING =
            new Color(243, 156, 18);

    public static final Color BACKGROUND =
            new Color(245, 247, 250);

    public static final Color WHITE =
            Color.WHITE;

    public static final Color TEXT =
            new Color(45, 45, 45);

    public static final Color MUTED =
            new Color(110, 110, 110);

    public static final Color BORDER =
            new Color(225, 225, 225);


    // ================= FONTS =================

    public static final Font TITLE_FONT =
            new Font("Arial", Font.BOLD, 26);

    public static final Font SUBTITLE_FONT =
            new Font("Arial", Font.PLAIN, 14);

    public static final Font NORMAL_FONT =
            new Font("Arial", Font.PLAIN, 14);

    public static final Font BOLD_FONT =
            new Font("Arial", Font.BOLD, 14);


    // ================= PANEL =================

    public static JPanel createCardPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        return panel;
    }


    // ================= BUTTON =================

    public static void styleButton(
            JButton button,
            Color background
    ) {

        button.setBackground(background);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 16, 8, 16
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }


    // ================= TABLE =================

    public static void styleTable(JTable table) {

        table.setRowHeight(34);

        table.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        table.setSelectionBackground(
                new Color(220, 235, 250)
        );

        table.setSelectionForeground(TEXT);

        table.setGridColor(
                new Color(235, 235, 235)
        );

        table.setShowVerticalLines(false);

        table.setIntercellSpacing(
                new Dimension(0, 1)
        );

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        table.getTableHeader().setBackground(
                new Color(52, 73, 94)
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();

        renderer.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 8, 0, 8
                )
        );

        table.setDefaultRenderer(
                Object.class,
                renderer
        );
    }


    // ================= TITLE PANEL =================

    public static JPanel createHeader(
            String title,
            String subtitle
    ) {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT);

        JLabel subtitleLabel =
                new JLabel(subtitle);

        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(MUTED);

        panel.add(titleLabel);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(subtitleLabel);

        return panel;
    }
}