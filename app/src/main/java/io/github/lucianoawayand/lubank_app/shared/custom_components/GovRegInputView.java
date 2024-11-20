package io.github.lucianoawayand.lubank_app.shared.custom_components;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import io.github.lucianoawayand.lubank_app.R;
import io.github.lucianoawayand.lubank_app.shared.utils.MaskEditUtil;

public class GovRegInputView extends LinearLayout {

    private EditText govRegCodeInput;
    private RadioGroup radioGroupCpfCnpj;
    private RadioButton radioButtonCpf;
    private RadioButton radioButtonCnpj;
    private TextWatcher textWatcher;

    public GovRegInputView(Context context) {
        super(context);
        init(context);
    }

    public GovRegInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GovRegInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.gov_reg_code_input, this, true);

        govRegCodeInput = findViewById(R.id.govRegCode_input);
        radioGroupCpfCnpj = findViewById(R.id.radioGroupCpfCnpj);
        radioButtonCpf = findViewById(R.id.radioButtonCpf);
        radioButtonCnpj = findViewById(R.id.radioButtonCnpj);

        handleCnpjCpfToggle();
    }

    private void setMaskAndLength(String mask, int maxLength) {
        govRegCodeInput.removeTextChangedListener(textWatcher);
        textWatcher = MaskEditUtil.mask(govRegCodeInput, mask);
        govRegCodeInput.addTextChangedListener(textWatcher);
        govRegCodeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    private void handleCnpjCpfToggle() {
        setMaskAndLength(MaskEditUtil.FORMAT_CPF, 14);

        radioGroupCpfCnpj.setOnCheckedChangeListener((group, checkedId) -> {
            govRegCodeInput.removeTextChangedListener(textWatcher);

            if (checkedId == R.id.radioButtonCpf) {
                setMaskAndLength(MaskEditUtil.FORMAT_CPF, 14);
                govRegCodeInput.setHint("CPF");
            } else if (checkedId == R.id.radioButtonCnpj) {
                setMaskAndLength(MaskEditUtil.FORMAT_CNPJ, 18);
                govRegCodeInput.setHint("CNPJ");
            }

            govRegCodeInput.setText("");
            govRegCodeInput.addTextChangedListener(textWatcher);
        });
    }
}
