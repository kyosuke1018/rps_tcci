/* common script */
function trimInput(input) {
    input.value = $.trim(input.value);
}

function trimInputToLower(input) {
    input.value = $.trim(input.value).toLowerCase();
}

function textDelayChange(filterId, filterLastId, callback) {
    // 300 milliseconds delay
    textDelayChange(filterId, filterLastId, callback, 300);
}

function textDelayChange(filterId, filterLastId, callback, delay) {
    $(filterId).bind('input keyup', function() {
        var $this = $(this);
        clearTimeout($this.data('timer'));
        $this.data('timer', setTimeout(function() {
            $this.removeData('timer');
            var userFilterLast = $(filterLastId).val();
            var userFilter = $.trim($(filterId).val());
            if (userFilterLast !== userFilter) {
                $(filterLastId).val(userFilter);
                callback();
            }
        }, delay));
    });
}
