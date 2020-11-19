
// Wifi Mac address 포맷 체크
module.exports.checkValidMacAddress = function(addr){
    var re = /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/;
    return re.test(addr);
};