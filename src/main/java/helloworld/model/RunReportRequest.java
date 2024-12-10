package helloworld.model;

public class RunReportRequest {
    private String idAccount;
    private String fechaInicial;
    private String fechaFinal;

    public RunReportRequest(String idAccount, String fechaInicial, String fechaFinal) {
        this.idAccount = idAccount;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public RunReportRequest() {
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}

