
require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'ehr', 'protocol', function (helper, scriptErrors, row, oldRow) {

    if (row.description) {
        plainTextParts = row.description.split("\\loch\\af0")
        let plainText = "";
        for(let i = 0; i < plainTextParts.length; i++)
        {
            if (i > 0)
            {
                let chunk = LABKEY.Utils.encodeHtml(plainTextParts[i].split("}")[0]);
                chunk = chunk.replace("\\hich\\af0 \\&#39;85", "&#8230;"); // ellipsis
                chunk = chunk.replace("\\hich\\af0 \\&#39;92", "'");
                chunk = chunk.replace("\\hich\\af0 \\&#39;93", "\"");
                chunk = chunk.replace("\\hich\\af0 \\&#39;94", "\"");
                chunk = chunk.replace("\\hich\\af0 \\&#39;99", "&#8482;"); // trademark
                chunk = chunk.replace("\\hich\\af0 \\&#39;ae", "&#174;");  // registered trademark
                chunk = chunk.replace("\\hich\\af0 \\&#39;b0", "&#176;"); // Degree sign
                chunk = chunk.replace("\\hich\\af0 \\&#39;b5", "&#181;"); // Micro sign (Greek mu)
                chunk = chunk.replace("\\hich\\af0 \\&#39;b7", "&#183;"); // Middle dot
                chunk = chunk.replace("\\hich\\af0 \\&#39;ef", "&#239;"); // i-diaeresis
                // TODO: As a general catch all, could convert the hex value in the match to the HTML ascii entity
                // Currently this covers all the cases though in the source data

                plainText += chunk.trim();
                plainText += " ";
            }
        }
        row.description = plainText.trim();
        // console.log(row.description);
    }

});