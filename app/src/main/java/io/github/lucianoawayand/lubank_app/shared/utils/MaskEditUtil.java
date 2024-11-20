package io.github.lucianoawayand.lubank_app.shared.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskEditUtil {
    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_CNPJ = "##.###.###/####-##";
    public static final String FORMAT_FONE = "(###)####-#####";
    public static final String FORMAT_CEP = "#####-###";
    public static final String FORMAT_DATE = "##/##/####";
    public static final String FORMAT_HOUR = "##:##";
    public static final String FORMAT_MONEY = "R$ #,##0.00"; // Currency format

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt EditText to format
     * @param mask   Mask to apply
     * @return TextWatcher for applying the mask
     */
    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating = false;
            String previousText = "";

            @Override
            public void afterTextChanged(final Editable s) {
                // No action needed here
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // Save the previous state in case of user edits
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if (isUpdating) return;

                String unmaskedText = unmask(s.toString());
                String formattedText;

                try {
                    isUpdating = true;

                    if (mask.equals(FORMAT_MONEY)) {
                        // Parse unmasked text as a long to avoid floating-point issues
                        long value = unmaskedText.isEmpty() ? 0 : Long.parseLong(unmaskedText);
                        formattedText = (String) formatMoney(value / 100.0); // Convert to "real" amount
                    } else {
                        // Apply other masks (CPF, CNPJ, etc.)
                        formattedText = applyMask(unmaskedText, mask);
                    }

                    // Update the EditText
                    ediTxt.setText(formattedText);
                    ediTxt.setSelection(formattedText.length());
                } catch (Exception e) {
                    // Restore the previous state if an error occurs
                    ediTxt.setText(previousText);
                    ediTxt.setSelection(previousText.length());
                } finally {
                    isUpdating = false;
                }
            }
        };
    }

    // Method to unmask the string (remove formatting characters)
    public static String unmask(final String s) {
        return s.replaceAll("[.]", "")
                .replaceAll("[-]", "")
                .replaceAll("[/]", "")
                .replaceAll("[(]", "")
                .replaceAll("[ ]", "")
                .replaceAll("[:]", "")
                .replaceAll("[)]", "")
                .replaceAll("[R$]", "")
                .replaceAll(",", "");
    }

    // Method to format the money string
    public static CharSequence formatMoney(Double value) {
        if (value == null) return "R$ 0,00";

        java.text.DecimalFormat format = new java.text.DecimalFormat("R$ #,##0.00");
        return format.format(value);
    }

    // Apply other types of masks (e.g., CPF, CNPJ)
    private static String applyMask(String unmaskedText, String mask) {
        String formattedText = "";
        int i = 0;

        for (char m : mask.toCharArray()) {
            if (m != '#' && unmaskedText.length() > i) {
                formattedText += m;
            } else {
                try {
                    formattedText += unmaskedText.charAt(i);
                } catch (Exception e) {
                    break;
                }
                i++;
            }
        }
        return formattedText;
    }
}
