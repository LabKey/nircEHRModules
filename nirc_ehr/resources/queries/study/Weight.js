require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.decodeExtraContextProperty('skipIdNotFoundError');

    if (helper.getProperty('skipIdNotFoundError')['form'] === 'arrival'||
            helper.getProperty('skipIdNotFoundError')['form'] === 'birth') {
        helper.setScriptOptions({
            allowAnyId: true
        });
    }
}