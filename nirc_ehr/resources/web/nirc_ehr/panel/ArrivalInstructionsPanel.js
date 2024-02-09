Ext4.define('NIRC_EHR.panel.ArrivalInstructionsPanel', {
    extend: 'NIRC_EHR.panel.InstructionsPanel',
    alias: 'widget.nirc_ehr-arrivalinstructionspanel',

    getItems: function(){
        return {
            html: 'This form allows you to enter new arrivals to the center.  On arrival, the following steps will occur:<p>' +
                    '<ul>' +
                    '<li>If not already present, a demographics record will be created.  This is the table holding 1 row for all known IDs.</li>' +
                    '<li>If birth, species, gender, and/or source facility are entered at the time the arrival is entered, the demographic record will use these. Editing the arrival record after the fact will automatically update the demographics table.</li>' +
                    '<li>If birth date is entered, a birth record will be created.</li>' +
                    '<li>If dam and/or sire are provided, the demographic and pedigree records will use these.</li>' +
                    '</ul>',
            style: 'padding: 5px;'
        }
    }
});