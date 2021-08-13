var triggers = require("ehr/triggers");
triggers.initScript(this);

function onUpsert(helper, scriptErrors, row, oldRow) {
    // this is to override the parent trigger concatenation of room and cage in location which is overflowing the underlying datatype
    row.location = row.status;
}