package io.github.lucianoawayand.lubank_app.shared.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

    /**
     * Formats a string as CPF.
     *
     * @param cpf Unformatted CPF string (only digits)
     * @return Formatted CPF string (e.g., 123.456.789-00)
     */
    public static String formatCPF(String cpf) {
        if (cpf == null) return "";
        String unmaskedCpf = unmask(cpf); // Remove any existing formatting
        return applyMask(unmaskedCpf, FORMAT_CPF);
    }

    /**
     * Formats a string as CNPJ.
     *
     * @param cnpj Unformatted CNPJ string (only digits)
     * @return Formatted CNPJ string (e.g., 12.345.678/0001-99)
     */
    public static String formatCNPJ(String cnpj) {
        if (cnpj == null) return "";
        String unmaskedCnpj = unmask(cnpj); // Remove any existing formatting
        return applyMask(unmaskedCnpj, FORMAT_CNPJ);
    }

    /**
     * Formats a date string in ISO 8601 format to "HH:MM, DD/MM/YYYY".
     *
     * @param isoDate The ISO 8601 date string (e.g., "2024-11-20T21:05:24.230Z")
     * @return Formatted date string or an empty string if parsing fails
     */
    public static String formatIsoDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";

        // Input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

        try {
            // Parse the input date string
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // ISO 8601 is in UTC
            Date date = inputFormat.parse(isoDate);

            // Format to desired output
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return empty string if parsing fails
        }
    }

    public static Double parseMoneyToDouble(String formattedValue) {
        if (formattedValue == null || formattedValue.isEmpty()) {
            return 0.0; // Default to 0.0 for empty or null input
        }

        try {
            // Remove formatting symbols and convert to a Double
            String numericValue = formattedValue
                    .replace("R$", "") // Remove currency symbol
                    .replace(".", "")  // Remove thousand separator
                    .replace(",", ".") // Replace decimal comma with dot
                    .trim();           // Remove leading/trailing whitespace
            return Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0; // Fallback to 0.0 on error
        }
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
