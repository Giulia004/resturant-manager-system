import React, { useState, useEffect } from 'react';
import axios from 'axios';

// 1. Definizione dell'interfaccia per TypeScript
interface User {
    id?: number;
    username: string;
    email: string;
    role: 'ADMIN' | 'USER' | 'CAMERIERE';
}

interface ControlPanelProps {
    onBack: () => void;
}

const ControlPanel: React.FC<ControlPanelProps> = ({ onBack }) => {
    const [users, setUsers] = useState<User[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentUser, setCurrentUser] = useState<User>({ username: '', email: '', role: 'USER' });
    const [isEditing, setIsEditing] = useState(false);

    // 2. Fetch degli utenti dal backend Spring Boot
    const fetchUsers = async () => {
        try {
            const response = await axios.get('/api/users');
            setUsers(response.data);
        } catch (error) {
            console.error("Errore nel caricamento utenti", error);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // 3. Gestione Salvataggio (Create / Update)
    const handleSave = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await axios.put(`/api/users/${currentUser.id}`, currentUser);
            } else {
                await axios.post('/api/users', currentUser);
            }
            setIsModalOpen(false);
            fetchUsers(); // Refresh della lista
        } catch (error) {
            alert("Errore durante il salvataggio");
        }
    };

    // 4. Gestione Eliminazione
    const handleDelete = async (id: number) => {
        if (window.confirm("Sei sicuro di voler eliminare questo utente?")) {
            await axios.delete(`/api/users/${id}`);
            fetchUsers();
        }
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <div className="flex items-center gap-4">
                    <button onClick={onBack} className="bg-gray-500 text-white px-3 py-1 rounded hover:bg-gray-600">← Indietro</button>
                    <h2 className="text-2xl font-bold">Gestione Utenti</h2>
                </div>
                <button
                    onClick={() => { setIsEditing(false); setCurrentUser({ username: '', email: '', role: 'USER' }); setIsModalOpen(true); }}
                    className="bg-blue-600 text-white px-4 py-2 rounded"
                >
                    + Nuovo Utente
                </button>
            </div>

            {/* Tabella Utenti */}
            <table className="min-w-full border-collapse block md:table">
                <thead className="block md:table-header-group">
                    <tr className="border border-gray-300 block md:table-row">
                        <th className="p-2 text-left block md:table-cell">Username</th>
                        <th className="p-2 text-left block md:table-cell">Email</th>
                        <th className="p-2 text-left block md:table-cell">Ruolo</th>
                        <th className="p-2 text-left block md:table-cell">Azioni</th>
                    </tr>
                </thead>
                <tbody className="block md:table-row-group">
                    {users.map(user => (
                        <tr key={user.id} className="bg-white border border-gray-300 block md:table-row">
                            <td className="p-2 block md:table-cell">{user.username}</td>
                            <td className="p-2 block md:table-cell">{user.email}</td>
                            <td className="p-2 block md:table-cell">
                                <span className="px-2 py-1 bg-gray-100 rounded text-sm">{user.role}</span>
                            </td>
                            <td className="p-2 block md:table-cell">
                                <button onClick={() => { setCurrentUser(user); setIsEditing(true); setIsModalOpen(true); }} className="text-blue-500 mr-2">Modifica</button>
                                <button onClick={() => handleDelete(user.id!)} className="text-red-500">Elimina</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Modale Semplificato */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <form onSubmit={handleSave} className="bg-white p-8 rounded-lg shadow-xl w-96">
                        <h3 className="text-xl mb-4">{isEditing ? 'Modifica Utente' : 'Nuovo Utente'}</h3>
                        <input
                            type="text" placeholder="Username"
                            className="w-full border p-2 mb-4"
                            value={currentUser.username}
                            onChange={e => setCurrentUser({ ...currentUser, username: e.target.value })}
                        />
                        <input
                            type="email" placeholder="Email"
                            className="w-full border p-2 mb-4"
                            value={currentUser.email}
                            onChange={e => setCurrentUser({ ...currentUser, email: e.target.value })}
                        />
                        <select
                            className="w-full border p-2 mb-4"
                            value={currentUser.role}
                            onChange={e => setCurrentUser({ ...currentUser, role: e.target.value as any })}
                        >
                            <option value="USER">User</option>
                            <option value="MANAGER">Manager</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <div className="flex justify-end gap-2">
                            <button type="button" onClick={() => setIsModalOpen(false)} className="bg-gray-300 px-4 py-2 rounded">Annulla</button>
                            <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded">Salva</button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
};

export default ControlPanel;