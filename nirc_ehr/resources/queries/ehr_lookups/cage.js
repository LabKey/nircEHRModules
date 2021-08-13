var triggers = require("ehr/triggers");
triggers.initScript(this);

function onUpsert(helper, scriptErrors, row, oldRow) {
    row.location = row.status;
}