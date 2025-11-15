import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class PasswordStrengthCheckerGUI {
    private boolean isPasswordVisible = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordStrengthCheckerGUI::new);
    }
    public PasswordStrengthCheckerGUI() {
        JFrame frame = new JFrame("🔒 Password Strength Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLayout(new BorderLayout());
        JLabel title = new JLabel("Password Strength Checker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel prompt = new JLabel("Enter Password:");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton eyeButton = new JButton("👁️");
        eyeButton.setFocusable(false);
        eyeButton.setPreferredSize(new Dimension(50, 30));
        JProgressBar progressBar = new JProgressBar(0, 5);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(250, 25));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(Color.DARK_GRAY);
        JLabel hintLabel = new JLabel(" ", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(prompt, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        centerPanel.add(passwordField, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        centerPanel.add(eyeButton, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        centerPanel.add(progressBar, gbc);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(resultLabel, BorderLayout.SOUTH);
        frame.add(hintLabel, BorderLayout.NORTH);
        eyeButton.addActionListener(e -> {
            if (isPasswordVisible) {
                passwordField.setEchoChar('•');
                isPasswordVisible = false;
            } else {
                passwordField.setEchoChar((char) 0);
                isPasswordVisible = true;
            }
        });
        Runnable updateStrength = () -> {
            String password = new String(passwordField.getPassword());
            int strength = calculateStrength(password);
            String message;
            Color color;
            switch (strength) {
                case 5:
                    message = "Very Strong 💪";
                    color = new Color(0, 150, 0);
                    break;
                case 4:
                    message = "Strong ✅";
                    color = new Color(0, 200, 0);
                    break;
                case 3:
                    message = "Weak ❌";
                    color = Color.RED;
                    break;
                default:
                    message = "Very Weak ❌❌";
                    color = Color.RED.darker();
                    break;
            }
            resultLabel.setText("<html><b>Strength:</b> " + message + "</html>");
            resultLabel.setForeground(color);
            progressBar.setValue(strength);
            progressBar.setForeground(color);
            hintLabel.setText(generateHint(password));
        };
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateStrength.run();
            }
        });
        passwordField.setEchoChar('•');
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private int calculateStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[@$!%*?&].*")) strength++;
        return strength;
    }
    private String generateHint(String password) {
        StringBuilder hint = new StringBuilder("<html><center>");
        if (password.length() < 8)
            hint.append("🔹 Add at least 8 characters<br>");
        if (!password.matches(".*[A-Z].*"))
            hint.append("🔹 Add an uppercase letter<br>");
        if (!password.matches(".*[0-9].*"))
            hint.append("🔹 Include a number<br>");
        if (!password.matches(".*[@$!%*?&].*"))
            hint.append("🔹 Add a special symbol (e.g., @, #, $)<br>");
        if (hint.length() == 14) 
            hint.append("✅ Perfect password!");
        hint.append("</center></html>");
        return hint.toString();
    }
}
