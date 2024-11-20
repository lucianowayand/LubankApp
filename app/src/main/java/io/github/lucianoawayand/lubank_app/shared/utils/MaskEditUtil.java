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

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return TextWatcher for applying the mask
     */
    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating = false;
            String previousText = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                // Avoid recursion if we are already updating
                if (isUpdating) return;

                String unmaskedText = unmask(s.toString());
                String formattedText = "";
                int i = 0;

                // Dynamically switch mask if CPF is longer than 11 characters
                String currentMask = mask;
                if (unmaskedText.length() > 11 && mask.equals(FORMAT_CPF)) {
                    currentMask = FORMAT_CNPJ;
                }

                // Format based on the selected mask
                for (char m : currentMask.toCharArray()) {
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

                // Prevent infinite recursion
                isUpdating = true;
                ediTxt.setText(formattedText);
                ediTxt.setSelection(formattedText.length());
                isUpdating = false;
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "")
                .replaceAll("[-]", "")
                .replaceAll("[/]", "")
                .replaceAll("[(]", "")
                .replaceAll("[ ]", "")
                .replaceAll("[:]", "")
                .replaceAll("[)]", "");
    }
}
