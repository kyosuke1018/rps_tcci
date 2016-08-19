/* common script */
function trimInput(input) {
    input.value = $.trim(input.value);
}

function trimInputToLower(input) {
    input.value = $.trim(input.value).toLowerCase();
}

function textDelayChange(inputText, callback) {
    // 300 milliseconds delay
    textDelayChange(inputText, callback, 300);
}

function textDelayChange(inputText, callback, delay) {
    $(inputText).on('input keyup', function() {
        var $this = $(this);
        clearTimeout($this.data('timer'));
        var valueLast;
        $this.data('timer', setTimeout(function() {
            $this.removeData('timer');
            var value = $.trim($this.val());
            if (valueLast !== value) {
                valueLast = value;
                callback();
            }
        }, delay));
    });
}
